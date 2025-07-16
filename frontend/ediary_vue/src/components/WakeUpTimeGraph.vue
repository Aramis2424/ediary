<script lang="ts" setup>
import type { MoodTimeGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import TimeGraph from '@/components/TimeGraph.vue'

defineProps<{
  wakeUpData: MoodTimeGraph[]
}>()

function timeToMillis(time: string): string {
  const [h, m] = time.split(':').map(Number)
  return String(h * 60 * 60 * 1000 + m * 60 * 1000)
}

function mapTime(data: MoodTimeGraph[]): MoodTimeGraph[] {
  return data.map(it => ({
        date: it.date,
        time: timeToMillis(it.time)
      }))
}

</script>

<template>
  
<div class="overflow-x-auto">
  <TimeGraph  :time-data="mapTime(wakeUpData)"
              border-color="rgb(75, 192, 192)"
              bg-color="rgba(75, 192, 192, 0.3)"
              point-style="triangle"
              title="Время подъема по дням"
              title-x="Дата"
              title-y="Время подъема"
              :min-y="0 * 60 * 60 * 1000"
              :max-y="24 * 60 * 60 * 1000"/>
</div>
  
</template>
