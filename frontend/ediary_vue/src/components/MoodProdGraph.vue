<script lang="ts" setup>
import type { MoodScoreGraph } from '@/types/Mood'
import 'chartjs-adapter-date-fns'
import ScoreGraph from '@/components/ScoreGraph.vue'

defineProps<{
  scoreData: MoodScoreGraph[]
}>()

</script>

<template>
  
<div class="overflow-x-auto">
  <ScoreGraph :score-data="scoreData"
              border-color-mood="rgb(54, 162, 235)"
              border-color-prod="rgb(255, 99, 132)"
              bg-color-mood="rgba(54, 192, 235, 0.3)"
              bg-color-prod="rgba(255, 99, 132, 0.3)"
              point-style-mood="circle"
              point-style-prod="rectRot"
              title="Настроение и продуктивность по дням"
              title-x="Дата"
              title-y="Оценка"
              label-mood="Настроение"
              label-prod="Продуктивность"
              :min-y="1"
              :max-y="10"/>
</div>
  
</template>
