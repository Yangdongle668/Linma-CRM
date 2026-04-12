<template>
  <div ref="chartRef" class="chart-container" :style="{ height: height + 'px' }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'

interface RankingData {
  name: string
  value: number
}

interface ProductRankingChartProps {
  data: RankingData[]
  height?: number
}

const props = withDefaults(defineProps<ProductRankingChartProps>(), {
  height: 350,
})

const chartRef = ref<HTMLElement>()
let chartInstance: ECharts | null = null

const initChart = () => {
  if (!chartRef.value) return

  chartInstance = echarts.init(chartRef.value)

  const option = {
    title: {
      text: '产品销量排行',
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
      formatter: (params: any) => {
        const data = params[0]
        return `${data.name}<br/>销量: ${data.value}`
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
      data: props.data.map((item) => item.name).reverse(),
      axisLine: {
        lineStyle: {
          color: '#E5E7EB',
        },
      },
      axisLabel: {
        color: '#6B7280',
      },
    },
    series: [
      {
        name: '销量',
        type: 'bar',
        data: props.data.map((item) => item.value).reverse(),
        barWidth: '60%',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            {
              offset: 0,
              color: '#667eea',
            },
            {
              offset: 1,
              color: '#764ba2',
            },
          ]),
          borderRadius: [0, 4, 4, 0],
        },
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
        data: props.data.map((item) => item.name).reverse(),
      },
      series: [
        {
          data: props.data.map((item) => item.value).reverse(),
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
