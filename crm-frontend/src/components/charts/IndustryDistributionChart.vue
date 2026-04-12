<template>
  <div ref="chartRef" class="chart-container" :style="{ height: height + 'px' }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'

interface ChartData {
  name: string
  value: number
}

interface IndustryDistributionChartProps {
  data: ChartData[]
  height?: number
}

const props = withDefaults(defineProps<IndustryDistributionChartProps>(), {
  height: 350,
})

const chartRef = ref<HTMLElement>()
let chartInstance: ECharts | null = null

const colors = [
  '#667eea', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6',
  '#EC4899', '#14B8A6', '#F97316', '#06B6D4', '#84CC16',
]

const initChart = () => {
  if (!chartRef.value) return

  chartInstance = echarts.init(chartRef.value)

  const option = {
    title: {
      text: '行业分布TOP10',
      left: 'center',
      textStyle: {
        fontSize: 14,
        fontWeight: 600,
        color: '#1F2937',
      },
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#E5E7EB',
      borderWidth: 1,
      textStyle: {
        color: '#1F2937',
      },
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true,
    },
    xAxis: {
      type: 'value',
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      splitLine: {
        lineStyle: {
          color: '#F3F4F6',
        },
      },
      axisLabel: {
        color: '#6B7280',
      },
    },
    yAxis: {
      type: 'category',
      data: [...props.data].reverse().map((item) => item.name),
      axisLine: {
        lineStyle: {
          color: '#E5E7EB',
        },
      },
      axisLabel: {
        color: '#4B5563',
      },
    },
    series: [
      {
        name: '客户数量',
        type: 'bar',
        barWidth: '60%',
        data: [...props.data].reverse().map((item, index) => ({
          value: item.value,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
              {
                offset: 0,
                color: colors[index % colors.length],
              },
              {
                offset: 1,
                color: colors[index % colors.length] + '80',
              },
            ]),
            borderRadius: [0, 6, 6, 0],
          },
        })),
        label: {
          show: true,
          position: 'right',
          color: '#6B7280',
          fontSize: 12,
        },
      },
    ],
  }

  chartInstance.setOption(option)
}

const resizeChart = () => {
  chartInstance?.resize()
}

watch(() => props.data, () => {
  if (chartInstance) {
    chartInstance.setOption({
      yAxis: {
        data: [...props.data].reverse().map((item) => item.name),
      },
      series: [
        {
          data: [...props.data].reverse().map((item, index) => ({
            value: item.value,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                {
                  offset: 0,
                  color: colors[index % colors.length],
                },
                {
                  offset: 1,
                  color: colors[index % colors.length] + '80',
                },
              ]),
              borderRadius: [0, 6, 6, 0],
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
