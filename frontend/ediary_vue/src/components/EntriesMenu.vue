<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import EntryCard from '@/components/EntryCard.vue';

const router = useRouter()
const entries = ref<{ id: number; title: string; date: string; mood: number, work: number }[]>([])

onMounted(() => {
  entries.value = [
    { id: 1, title: "Много разговоров", date: "2020-01-06", mood: 5, work: 8 },
    { id: 2, title: "Отдых", date: "2020-01-07", mood: 7, work: 9 },
    { id: 3, title: "Начал за здравие ...", date: "2020-01-08", mood: 10, work: 4 },
    { id: 4, title: "7 часов ...", date: "2020-01-09", mood: 3, work: 2 },
    { id: 5, title: "Работал за двоих", date: "2020-01-10", mood: 1, work: 10 },
    { id: 6, title: "Отстучал пальцы", date: "2020-01-11", mood: 8, work: 8 },
    { id: 7, title: "Пол дня из жизни", date: "2020-01-12", mood: 7, work: 4 },
    { id: 8, title: "Работа и работа", date: "2020-01-15", mood: 6, work: 9 },
  ]
})

function gotoEntry(id: number) {
  router.push(`entry/${id}`)
}
</script>

<template>
<div class="h-screen flex justify-center items-center bg-fire">
  <div class="h-[90vh] w-full max-w-5xl flex flex-wrap gap-5 justify-center content-start p-4 overflow-y-scroll hide-scrollbar">
    <EntryCard v-for="entry in entries" :key="entry.id" :entry="entry" @clicked="gotoEntry(entry.id)" />
  </div>
</div>
</template>

<style scoped>
</style>
