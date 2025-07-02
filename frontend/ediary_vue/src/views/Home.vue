<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useDiaryStore } from '@/stores/diaries';
import { ref, watch, type Ref } from 'vue';
import { api } from '@/api/axios';
import Settings from './Settings.vue';
import AboutYourself from './AboutYourself.vue';
import type { DiaryCreateDTO, DiaryInfoDTO } from '@/types/Diary';
import { useAuthStore } from '@/stores/auth';
import type { AxiosResponse } from 'axios';

const showSettings = ref(false);
const showAboutYourself = ref(false);
const router = useRouter();
const owner = useAuthStore();
const diaryStore = useDiaryStore()

const gotoEntriesMenu = () => {router.push('/menu')}
const gotoMoodGraph = () => {router.push('/graph')}

const ownerName: Ref<string> = ref('');
let diaryInfo: AxiosResponse<DiaryInfoDTO> | null = null

watch(
  () => owner.user,
  async (newOwner) => {
    if (!newOwner)
      return
    ownerName.value = newOwner.name
    try {
      diaryInfo = await api.get<DiaryInfoDTO>(`/owners/${newOwner.id}/diaries`)
    } catch {
      const newDiary: DiaryCreateDTO = {
        ownerId: newOwner.id,
        title: "New diary",
        description: "Description for new diary"
      }
      diaryInfo = await api.post<DiaryCreateDTO, 
          AxiosResponse<DiaryInfoDTO>>(`/diaries/`, newDiary)
    }
    diaryStore.logIn(diaryInfo!.data)
    },
  { immediate: true }
)

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
        <button @click="gotoMoodGraph()" class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
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
