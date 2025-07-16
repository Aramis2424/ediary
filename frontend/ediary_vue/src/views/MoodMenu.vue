<script lang="ts" setup>
import { useRouter } from 'vue-router'
import WakeUpTimeGraph from '@/components/WakeUpTimeGraph.vue'
import BedtimeGraph from '@/components/BedtimeGraph.vue';
import MoodProdGraph from '@/components/MoodProdGraph.vue';
import SurveyMood from '@/views/SurveyMood.vue';
import type { MoodInfoDTO, MoodScoreGraph, MoodTimeGraph } from '@/types/Mood';
import { computed, onMounted, ref } from 'vue';
import { fetchMoods, fetchPermissionMood } from '@/services/moodService';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const owner = useAuthStore();

const showSurveyMood = ref(false);
const enableCreateMood = ref(false);

async function surveyMood() {
  showSurveyMood.value = true
}

function toMoodScoreGraph(data: MoodInfoDTO[]): MoodScoreGraph[] {
  return data.map(entry => ({
    mood: entry.scoreMood,
    productivity: entry.scoreProductivity,
    date: entry.createdAt
  }));
}

function toTimeGraph(data: MoodInfoDTO[], type: 'wake' | 'bed'): MoodTimeGraph[] {
  return data.map(it => {
    const { time, date } = type === 'wake'
      ? parseDateTime(it.wakeUpTime)
      : parseDateTime(it.bedtime);
    return {
      time,
      date
    };
  });
}

function parseDateTime(dateTime: string): { date: string; time: string } {
  const [date, timeWithSeconds] = dateTime.split('T');
  const time = timeWithSeconds.slice(0, 5);
  return { date, time };
}

async function pullMoods() {
  if (!owner.user)
    return
  try {
    moodList.value = await fetchMoods(owner.user.id)
  } catch {
    console.error("Error while fetching moods");
  }
}

async function pullPermission() {
  if (!owner.user)
    return
  try {
    const result = await fetchPermissionMood(owner.user?.id);
    enableCreateMood.value = result.allowed;
  } catch {
    console.error("Error while fetching permission mood");
  }
}

const moodList = ref<MoodInfoDTO[]>([]);
onMounted(async () => {
 await pullMoods()
 await pullPermission()
})

const scoreData = computed(() => toMoodScoreGraph(moodList.value));
const wakeUpData = computed(() => toTimeGraph(moodList.value, 'wake'));
const bedtimeData = computed(() => toTimeGraph(moodList.value, 'bed'));

const gotoHome = () => {router.push('/home')}
</script>

<template>

<div class="h-screen w-full flex items-center px-2 bg-fire">
  <button class="sideBtnL" @click="gotoHome"> Назад </button>
  <div class="h-[90vh] w-full overflow-y-scroll hide-scrollbar">
    <div class="flex flex-col gap-y-16">
      <MoodProdGraph :scoreData="scoreData" />
      <WakeUpTimeGraph :wakeUpData="wakeUpData" />
      <BedtimeGraph :wakeUpData="bedtimeData" />
    </div>
  </div>
  <button v-if="enableCreateMood" class="sideBtnR" @click="surveyMood"> Отметить настроение </button>
  <button v-else class="diableSideBtnR"> Сегодня настроение уже отмечено </button>
</div>
<SurveyMood v-if="showSurveyMood" @clicked="showSurveyMood=false; pullMoods(); pullPermission();"/>

</template>
