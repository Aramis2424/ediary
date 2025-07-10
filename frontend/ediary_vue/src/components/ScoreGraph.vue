<script setup lang="ts">
import type { ChartData, ChartOptions } from 'chart.js'
import type { MoodScoreGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import { ref, computed } from 'vue'
import { parseISO, subDays, addDays } from 'date-fns'
import BaseGraph from '@/components/BaseGraph.vue'

const props = defineProps<{
  scoreData: MoodScoreGraph[]
  borderColorMood: string
  borderColorProd: string
  bgColorMood: string
  bgColorProd: string
  pointStyleMood: string
  pointStyleProd: string
  title: string
  titleX: string
  titleY: string
  labelMood: string
  labelProd: string
  minY: number
  maxY: number
}>()

const allDates = props.scoreData.map(it => parseISO(it.date))
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
      label: props.labelMood,
      data: props.scoreData.map(entry => ({ 
        x: String(entry.date),
        y: Number(entry.mood) })),
      borderColor: props.borderColorMood,
      backgroundColor: props.bgColorMood,
      fill: false,
      tension: 0.4,
      pointRadius: 6,
      pointHoverRadius: 8,
      pointStyle: props.pointStyleMood
    },
    {
      label: props.labelProd,
      data: props.scoreData.map(entry => ({ 
        x: String(entry.date), 
        y: Number(entry.productivity) })),
      borderColor: props.borderColorProd,
      backgroundColor: props.bgColorProd,
      fill: false,
      tension: 0.4,
      pointRadius: 6,
      pointHoverRadius: 8,
      pointStyle: props.pointStyleProd
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
      display: true,
      position: 'top' as const
    },
    tooltip: {
      mode: 'index',
      intersect: false
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
      suggestedMin: props.minY,
      suggestedMax: props.maxY,
      ticks: {
        stepSize: 1
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
