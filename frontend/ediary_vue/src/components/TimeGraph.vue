<script lang="ts" setup>
import type { ChartData, ChartOptions } from 'chart.js'
import type { MoodTimeGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import { ref, computed } from 'vue'
import { parseISO, subDays, addDays } from 'date-fns'
import BaseGraph from '@/components/BaseGraph.vue'

const props = defineProps<{
  timeData: MoodTimeGraph[]
  borderColor: string
  bgColor: string
  pointStyle: string
  title: string
  titleX: string
  titleY: string
  minY: number
  maxY: number
}>()

const allDates = props.timeData.map(d => parseISO(d.date))
const initialMax = allDates[allDates.length - 1]
const maxDate = ref(initialMax)
const minDate = computed(() => subDays(maxDate.value, 6))

function shiftWindow(direction: 'back' | 'forward') {
  if (direction === 'back') {
    maxDate.value = subDays(maxDate.value, 7)
  } else {
    maxDate.value = addDays(maxDate.value, 7)
  }
}

const chartData = computed(() => ({
  datasets: [
    {
      label: 'DateTime',
      data: props.timeData.map(it => ({
        x: it.date,
        y: Number(it.time)
      })),
      borderColor: props.borderColor,
      backgroundColor: props.bgColor,
      fill: false,
      tension: 0.4,
      pointRadius: 6,
      pointHoverRadius: 8,
      pointStyle: props.pointStyle
    }
  ]
}) as unknown as ChartData<'line'>)

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: props.title,
    },
    legend: {
      display: false,
    },
    tooltip: {
      callbacks: {
        label: function (context: any) {
          const time = new Date(context.parsed.y).toISOString().substring(11, 16)
          return `${context.dataset.label}: ${time}`
        }
      }
    }
  },
  scales: {
    x: {
      type: 'time',
      min: minDate.value,
      max: maxDate.value,
      time: {
        unit: 'day',
        tooltipFormat: 'dd.MM.yyyy',
        displayFormats: {
          day: 'dd.MM'
        }
      },
      title: {
        display: true,
        text: props.titleX,
      }
    },
    y: {
      type: 'linear',
      min: props.minY,
      max: props.maxY,
      ticks: {
        stepSize: 30 * 60 * 1000,
        callback: (value: number) => {
          const date = new Date(value)
          return date.toISOString().substring(11, 16)
        }
      },
      title: {
        display: true,
        text: props.titleY,
      }
    }
  }
}) as unknown as ChartOptions<'line'>)
</script>

<template>
  
<div class="overflow-x-auto">
  <BaseGraph :data="chartData" :options="chartOptions" 
    @prev="shiftWindow('back')" @next="shiftWindow('forward')"/>
</div>
  
</template>
