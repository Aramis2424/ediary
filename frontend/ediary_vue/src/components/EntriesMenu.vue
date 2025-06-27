<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { api } from '@/api/axios';
import { useDiaryStore } from '../stores/diaries';
import EntryCard from '@/components/EntryCard.vue';
import EntriesSearching from './EntriesSearching.vue';
import NewEntryCard from './NewEntryCard.vue';
import SurveyMood from './SurveyMood.vue';
import type { Entry, EntryCreateDTO } from '@/types/Entry';
import type { EntryCard as EntryCardDto } from '@/types/EntryCard';
import type { AxiosResponse } from 'axios';

const router = useRouter();
const diary = useDiaryStore();

const entries = ref<EntryCardDto[]>([])
const showSearching = ref(false); 
const showSurveyMood = ref(false);

onMounted(async () => {
  const cards = await api.get<EntryCardDto[]>(`/entryCards/${diary.id}`)
  entries.value = cards.data;
})

async function createEntry(): Promise<void> {
  const newEntry: EntryCreateDTO = {
    diaryId: String(diary.id),
    title: "Новый день",
    content: ""
  }
  const res = await api.post<EntryCreateDTO, AxiosResponse<Entry>>('entries/', newEntry)
  gotoEntry(res.data.id);
}

async function createMood() {
  showSurveyMood.value = true
}

const gotoEntry = (id: number) => {router.push(`entry/${id}`)}
const gotoHome = () => {router.push('/home')}
</script>

<template>
<div class="h-screen w-full flex justify-between items-center px-2 bg-fire">
  <button class="sideBtnL" @click="gotoHome"> Назад </button>
  <div class="h-[90vh] w-full max-w-5xl flex flex-wrap gap-5 justify-center content-start p-4 overflow-y-scroll hide-scrollbar">
    <NewEntryCard @clicked="createMood" />
    <EntryCard v-for="entry in entries" :key="entry.entryId" :entry="entry" @clicked="gotoEntry(entry.entryId)" />
  </div>
  <button @click="showSearching = true" class="sideBtnR"> Поиск записей </button>
</div>

<EntriesSearching v-if="showSearching" @clicked="showSearching = false"/> 
<SurveyMood v-if="showSurveyMood" @clicked="createEntry"/>
</template>

<style scoped>
</style>
