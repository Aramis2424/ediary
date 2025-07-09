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

ChartJS.register(
  LineElement,
  PointElement,
  LinearScale,
  TimeScale,
  Title,
  Tooltip,
  Legend
)

defineProps<{
  data: ChartData<'line'>
  options: ChartOptions<'line'>
}>()

const emit = defineEmits<{
  (e: 'prev'): void
  (e: 'next'): void
}>()
</script>

<template>

<div class="flex flex-col justify-center items-center m-1">
    <div class="w-1/3 h-96 rounded-xl border-[#F4A261] border-2">
        <Line :data="data" :options="options" />
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
