<script lang="ts" setup>
import type { ChartData, ChartOptions } from 'chart.js'
import type { MoodTimeGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import { ref, computed } from 'vue'
import { parseISO, subDays, addDays } from 'date-fns'
import BaseGraph from '@/components/BaseGraph.vue'

const props = defineProps<{
  wakeUpData: MoodTimeGraph[]
}>()

const allDates = props.wakeUpData.map(d => parseISO(d.date))
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

function timeToMillis(time: string): number {
  const [h, m] = time.split(':').map(Number)
  return h * 60 * 60 * 1000 + m * 60 * 1000
}

const chartData = computed(() => ({
  datasets: [
    {
      label: 'Время подъема',
      data: props.wakeUpData.map(entry => ({
        x: entry.date,
        y: timeToMillis(entry.time)
      })),
      borderColor: 'rgb(75, 192, 192)',
      backgroundColor: 'rgba(75, 192, 192, 0.3)',
      fill: false,
      tension: 0.4,
      pointRadius: 6,
      pointHoverRadius: 8,
      pointStyle: 'triangle'
    }
  ]
}) as unknown as ChartData<'line'>)

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    title: {
      display: true,
      text: 'Время подъема по дням',
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
        text: 'Дата',
      }
    },
    y: {
      type: 'linear',
      min: 4 * 60 * 60 * 1000, // 04:00
      max: 12 * 60 * 60 * 1000, // 12:00
      ticks: {
        stepSize: 30 * 60 * 1000,
        callback: (value: number) => {
          const date = new Date(value)
          return date.toISOString().substring(11, 16)
        }
      },
      title: {
        display: true,
        text: 'Время подъема',
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
