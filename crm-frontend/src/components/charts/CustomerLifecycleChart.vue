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

interface CustomerLifecycleChartProps {
  data: ChartData[]
  height?: number
}

const props = withDefaults(defineProps<CustomerLifecycleChartProps>(), {
  height: 350,
})

const chartRef = ref<HTMLElement>()
let chartInstance: ECharts | null = null

const colors = ['#9CA3AF', '#667eea', '#F59E0B', '#10B981', '#EF4444']

const initChart = () => {
  if (!chartRef.value) return

  chartInstance = echarts.init(chartRef.value)

  const option = {
    title: {
      text: '客户生命周期分布',
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
      type: 'category',
      data: props.data.map((item) => item.name),
      axisLine: {
        lineStyle: {
          color: '#E5E7EB',
        },
      },
      axisLabel: {
        color: '#6B7280',
        interval: 0,
        rotate: 30,
      },
    },
    yAxis: {
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
    series: [
      {
        name: '客户数量',
        type: 'bar',
        barWidth: '50%',
        data: props.data.map((item, index) => ({
          value: item.value,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: colors[index % colors.length],
              },
              {
                offset: 1,
                color: colors[index % colors.length] + '80',
              },
            ]),
            borderRadius: [6, 6, 0, 0],
          },
        })),
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
      xAxis: {
        data: props.data.map((item) => item.name),
      },
      series: [
        {
          data: props.data.map((item, index) => ({
            value: item.value,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: colors[index % colors.length],
                },
                {
                  offset: 1,
                  color: colors[index % colors.length] + '80',
                },
              ]),
              borderRadius: [6, 6, 0, 0],
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
