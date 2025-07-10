<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useDiaryStore } from '@/stores/diaries';
import { ref, watch, type Ref } from 'vue';
import Settings from '@/views/Settings.vue';
import AboutYourself from '@/views/AboutYourself.vue';
import type { DiaryInfoDTO } from '@/types/Diary';
import { useAuthStore } from '@/stores/auth';
import { fetchDiary, createDiary } from '@/services/diaryService';

const router = useRouter();
const owner = useAuthStore();
const diaryStore = useDiaryStore()

const showSettings = ref(false);
const showAboutYourself = ref(false);

const ownerName: Ref<string> = ref('');
let diaryInfo: DiaryInfoDTO | null = null

watch(
  () => owner.user,
  async (ownerInfo) => {
    if (!ownerInfo)
      return
    ownerName.value = ownerInfo.name
    try {
      diaryInfo = await fetchDiary(ownerInfo.id)
    } catch {
      diaryInfo = await createDiary(ownerInfo.id)
    }
    if (diaryInfo)
      diaryStore.save(diaryInfo)
    else
      console.error("Error while creating or fetching diary");
    },
  { immediate: true }
)

const gotoEntriesMenu = () => {router.push('/menu')}
const gotoMoodMenu = () => {router.push('/mood')}
</script>

<template>
   <div class="h-screen overflow-auto flex flex-col items-center justify-between p-2 relative bg-lemon space-y-10">
    <h1 class="font-czizh text-white bg-[#B4B4B4] bg-opacity-10 text-6xl absolute top-4 left-1/2 -translate-x-1/2">
      Ваши мысли, {{ ownerName }}!
    </h1>
    <div class="flex flex-1 w-full items-center justify-between">
      <button @click="showAboutYourself = true" class="sideBtnL"> О себе </button>

      <div class="flex flex-col items-center justify-center gap-4 h-full w-full max-w-md">
        <button @click="gotoEntriesMenu()" class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
          Личный дневник
        </button>
        <button @click="gotoMoodMenu()" class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
          Настроения
        </button>
      </div>

      <button @click="showSettings = true" class="sideBtnR" style="letter-spacing: 2px;"> Настройки </button>
    </div>
  </div>

  <Settings v-if="showSettings" @clicked="showSettings = false"/> 
  <AboutYourself v-if="showAboutYourself" @clicked="showAboutYourself = false"/> 

</template>

<style scoped>
</style>
