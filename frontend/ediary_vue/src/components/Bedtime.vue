<script lang="ts" setup>
import type { MoodTimeGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import TimeGraph from '@/components/TimeGraph.vue'

defineProps<{
  wakeUpData: MoodTimeGraph[]
}>()

function timeToMillis(time: string): string {
  const [h, m] = time.split(':').map(Number);
  let totalMinutes = h * 60 + m;
  
  // Если заснул после полуночи — считаем как "ночь следующего дня"
  if (totalMinutes < 18 * 60) {
    totalMinutes += 24 * 60;
  }

  return String(totalMinutes * 60 * 1000);
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
              title="Время засыпания по дням"
              title-x="Дата"
              title-y="Время засыпания"
              :min-y="14 * 60 * 60 * 1000"
              :max-y="38 * 60 * 60 * 1000"/>
</div>
  
</template>
