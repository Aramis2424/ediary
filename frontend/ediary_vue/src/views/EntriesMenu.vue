<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useDiaryStore } from '@/stores/diaries';
import EntryCard from '@/components/EntryCard.vue';
import EntriesSearching from '@/views/EntriesSearching.vue';
import NewEntryCard from '@/components/NewEntryCard.vue';
import type { EntryInfoDTO } from '@/types/Entry';
import type { EntryCard as EntryCardType } from '@/types/EntryCard';
import { createEntry, fetchPermissionEntry } from '@/services/entryService';
import { fetchEntryCards } from '@/services/entryCardService';

const router = useRouter();
const diary = useDiaryStore();

const entries = ref<EntryCardType[]>([])
const showSearching = ref(false);
const enableCreateEntry = ref(false);

onMounted(async () => {
  try {
    const cards = await fetchEntryCards(diary.id)
    entries.value = cards;

    const result = await fetchPermissionEntry(diary.id);
    enableCreateEntry.value = result.allowed;
  } catch {
    console.error("Error while fetching entry cards");
  }
})

async function createNewEntry(): Promise<void> {
  try {
    const createdEntry: EntryInfoDTO = await createEntry(diary.id)
    gotoEntry(createdEntry.id);
  } catch {
    console.error("Error while creating entry");
  }
}

const gotoEntry = (id: number) => {router.push(`entry/${id}`)}
const gotoHome = () => {router.push('/home')}
</script>

<template>
<div class="h-screen w-full flex justify-between items-center px-2 bg-fire">
  <button class="sideBtnL" @click="gotoHome"> Назад </button>
  <div class="h-[90vh] w-full max-w-5xl flex flex-wrap gap-5 justify-center content-start p-4 overflow-y-scroll hide-scrollbar">
    <NewEntryCard @clicked="createNewEntry" :enable="enableCreateEntry"/>
    <EntryCard v-for="entry in entries" :key="entry.entryId" :entry="entry" @clicked="gotoEntry(entry.entryId)" />
  </div>
  <button @click="showSearching = true" class="sideBtnR"> Поиск записей </button>
</div>

<EntriesSearching v-if="showSearching" @clicked="showSearching = false"/> 
</template>

<style scoped>
</style>
