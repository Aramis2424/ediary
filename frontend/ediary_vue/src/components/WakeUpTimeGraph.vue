<script lang="ts" setup>
import type { MoodTimeGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import TimeGraph from '@/components/TimeGraph.vue'

defineProps<{
  wakeUpData: MoodTimeGraph[]
}>()

</script>

<template>
  
<div class="overflow-x-auto">
  <TimeGraph  :time-data="wakeUpData"
              border-color="rgb(75, 192, 192)"
              bg-color="rgba(75, 192, 192, 0.3)"
              point-style="triangle"
              title="Время подъема по дням"
              title-x="Дата"
              title-y="Время подъема"
              :min-y="4 * 60 * 60 * 1000"
              :max-y="12 * 60 * 60 * 1000"/>
</div>
  
</template>
