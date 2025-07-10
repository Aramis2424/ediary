<script lang="ts" setup>
import type { ChartData, ChartOptions } from 'chart.js'
import {
  Chart as ChartJS,
  LineElement,
  PointElement,
  LinearScale,
  TimeScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js'
import { Line } from 'vue-chartjs'
import { merge } from 'lodash-es'
import { watch } from 'vue'

ChartJS.register(
  LineElement,
  PointElement,
  LinearScale,
  TimeScale,
  Title,
  Tooltip,
  Legend
)

const baseStyle = {
    responsive: true,
    plugins: {
      title: {
        font: {
          size: 20,
          weight: 'bold'
        },
        padding: {
          top: 10,
          bottom: 30
        }
      },
      legend: {
        labels: {
          font: {
            size: 16
          }
        }
      }
    },
    scales: {
      x: {
        title: {
          font: {
            size: 16,
            weight: 'bold'
          }
        }
      },
      y: {
        title: {
          font: {
            size: 16,
            weight: 'bold'
          }
        }
      }
    }
  }

const props = defineProps<{
  data: ChartData<'line'>
  options: ChartOptions<'line'>
}>()

const emit = defineEmits<{
  (e: 'prev'): void
  (e: 'next'): void
}>()

let fullOptions: ChartOptions<'line'> = props.options;
watch(
    () => props.options,
    (updOptions) => {
        fullOptions = merge({}, updOptions, baseStyle)
    },
    { immediate: true }
)
</script>

<template>

<div class="flex flex-col justify-center items-center m-1">
    <div class="w-2/3 h-96">
        <Line :data="data" :options="fullOptions" class="bg-white opacity-80 rounded-xl border-[#F4A261] border-4" />
    </div>
    <div class="flex items-center justify-between my-1 gap-x-2">
        <button class="baseBtn px-2 w-24" @click="emit('prev')" >
        ← Назад
        </button>
        <button class="baseBtn px-2 w-24" @click="emit('next')" >
        Вперёд →
        </button>
    </div>
</div>

</template>
