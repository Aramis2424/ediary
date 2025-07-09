<script lang="ts" setup>
import WakeUpTimeGraph from '@/components/WakeUpTimeGraph.vue'

// --- Примерные данные ---
const wakeUpData = [
  { date: '2025-06-28', time: '07:30' },
  { date: '2025-06-29', time: '08:00' },
  { date: '2025-06-30', time: '07:45' },
  { date: '2025-07-01', time: '06:50' },
  { date: '2025-07-02', time: '07:10' },
  { date: '2025-07-03', time: '08:30' },
  { date: '2025-07-04', time: '07:05' },
  { date: '2025-07-05', time: '06:45' },
  { date: '2025-07-06', time: '07:20' },
  { date: '2025-07-07', time: '07:00' }
]

</script>

<template>
  
<div class="overflow-x-auto">
  <div class="flex flex-col">
    <WakeUpTimeGraph :wakeUpData="wakeUpData" />
    <WakeUpTimeGraph :wakeUpData="wakeUpData" />
  </div>
</div>

</template>
