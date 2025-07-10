<script lang="ts" setup>
import { useRouter } from 'vue-router'
import WakeUpTimeGraph from '@/components/WakeUpTimeGraph.vue'
import Bedtime from '@/components/BedtimeGraph.vue';
import MoodProdGraph from '@/components/MoodProdGraph.vue';
import SurveyMood from '@/views/SurveyMood.vue';
import type { MoodScoreGraph, MoodTimeGraph } from '@/types/Mood';
import { ref } from 'vue';

const router = useRouter();
const showSurveyMood = ref(false);

async function surveyMood() {
  showSurveyMood.value = true
}

// --- Примерные данные ---
const wakeUpData: MoodTimeGraph[] = [
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

const bedtimeData: MoodTimeGraph[] = [
  { date: '2025-06-28', time: '22:30' },
  { date: '2025-06-29', time: '23:00' },
  { date: '2025-06-30', time: '00:45' },
  { date: '2025-07-01', time: '23:50' },
  { date: '2025-07-02', time: '23:10' },
  { date: '2025-07-03', time: '22:30' },
  { date: '2025-07-04', time: '00:05' },
  { date: '2025-07-05', time: '22:45' },
  { date: '2025-07-06', time: '23:20' },
  { date: '2025-07-07', time: '00:00' }
]

const scoreData: MoodScoreGraph[] = [
  { date: '2025-06-28', mood: 5, productivity: 7 },
  { date: '2025-06-29', mood: 6, productivity: 6 },
  { date: '2025-06-30', mood: 5, productivity: 9 },
  { date: '2025-07-01', mood: 6, productivity: 4 },
  { date: '2025-07-02', mood: 7, productivity: 5 },
  { date: '2025-07-03', mood: 4, productivity: 8 },
  { date: '2025-07-04', mood: 8, productivity: 3 },
  { date: '2025-07-05', mood: 5, productivity: 5 },
  { date: '2025-07-06', mood: 9, productivity: 9 },
  { date: '2025-07-07', mood: 7, productivity: 8 }
]

const gotoHome = () => {router.push('/home')}
</script>

<template>

<div class="h-screen w-full flex items-center px-2 bg-fire">
  <button class="sideBtnL" @click="gotoHome"> Назад </button>
  <div class="h-[90vh] w-full overflow-y-scroll hide-scrollbar">
    <div class="flex flex-col gap-y-16">
      <MoodProdGraph :scoreData="scoreData" />
      <WakeUpTimeGraph :wakeUpData="wakeUpData" />
      <Bedtime :wakeUpData="bedtimeData" />
    </div>
  </div>
  <button class="sideBtnR" @click="surveyMood"> Отметить настроение </button>
</div>
<SurveyMood v-if="showSurveyMood" @clicked="showSurveyMood=false"/>

</template>
