<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { api } from '@/api/axios';
import { useOwnerStore } from '../stores/owner';
import EntryCard from '@/components/EntryCard.vue';
import type { EntryCard as EntryCardDto } from '@/types/EntryCard';

const router = useRouter();
const owner = useOwnerStore();

const entries = ref<EntryCardDto[]>([])

onMounted(async () => {
  const cards = await api.get<EntryCardDto[]>(`/entryCards/${owner.diaryId}`)
  entries.value = cards.data;
})

function gotoEntry(id: number) {
  router.push(`entry/${id}`)
}

const gotoHome = () => {router.push('/home')}
</script>

<template>
<div class="h-screen w-full flex justify-between items-center px-2 bg-fire">
  <button class="sideBtnL" @click="gotoHome"> Назад </button>
  <div class="h-[90vh] w-full max-w-5xl flex flex-wrap gap-5 justify-center content-start p-4 overflow-y-scroll hide-scrollbar">
    <EntryCard v-for="entry in entries" :key="entry.entryId" :entry="entry" @clicked="gotoEntry(entry.entryId)" />
  </div>
  <button class="sideBtnR"> Поиск записей </button>
</div>
</template>

<style scoped>
</style>
