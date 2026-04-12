<template>
  <div ref="chartRef" class="chart-container" :style="{ height: height + 'px' }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'

interface FunnelData {
  stage: string
  count: number
}

interface CustomerFunnelChartProps {
  data: FunnelData[]
  height?: number
}

const props = withDefaults(defineProps<CustomerFunnelChartProps>(), {
  height: 350,
})

const chartRef = ref<HTMLElement>()
let chartInstance: ECharts | null = null

const initChart = () => {
  if (!chartRef.value) return

  chartInstance = echarts.init(chartRef.value)

  const option = {
    title: {
      text: '客户转化漏斗',
      left: 'center',
      textStyle: {
        fontSize: 14,
        fontWeight: 600,
        color: '#1F2937',
      },
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#E5E7EB',
      borderWidth: 1,
      textStyle: {
        color: '#1F2937',
      },
      formatter: (params: any) => {
        return `${params.name}<br/>数量: ${params.value}<br/>占比: ${params.percent}%`
      },
    },
    series: [
      {
        name: '客户转化',
        type: 'funnel',
        left: '10%',
        top: 60,
        bottom: 60,
        width: '80%',
        min: 0,
        max: props.data[0]?.count || 100,
        minSize: '0%',
        maxSize: '100%',
        sort: 'descending',
        gap: 2,
        label: {
          show: true,
          position: 'inside',
          formatter: '{b}: {c}',
          color: '#fff',
          fontSize: 12,
        },
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1,
        },
        emphasis: {
          label: {
            fontSize: 14,
          },
        },
        data: props.data.map((item, index) => ({
          value: item.count,
          name: item.stage,
          itemStyle: {
            color: getColorByIndex(index),
          },
        })),
      },
    ],
  }

  chartInstance.setOption(option)
}

const getColorByIndex = (index: number): string => {
  const colors = ['#667eea', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6', '#EC4899']
  return colors[index % colors.length]
}

const resizeChart = () => {
  chartInstance?.resize()
}

watch(() => props.data, () => {
  if (chartInstance) {
    chartInstance.setOption({
      series: [
        {
          max: props.data[0]?.count || 100,
          data: props.data.map((item, index) => ({
            value: item.count,
            name: item.stage,
            itemStyle: {
              color: getColorByIndex(index),
            },
          })),
        },
      ],
    })
  }
}, { deep: true })

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  chartInstance?.dispose()
})
</script>

<style scoped lang="scss">
.chart-container {
  width: 100%;
}
</style>
