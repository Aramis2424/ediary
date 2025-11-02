<template>
<div class="baseBacking">
    <div class="bg-white rounded-xl p-2 w-1/6 h-1/6 gap-2 shadow-xl relative flex flex-col justify-evenly content-center items-end entriesSearching">
        <div class="w-full flex justify-center">
            <h2 class="text-xl font-semibold mb-4">Поиск записей</h2>
        </div>
        <SearchingField v-model="title" type="text" placeholder="Заголовок" label="Заголовок" />
        <SearchingField v-model="dateFrom" type="date" placeholder="С (включительно)" label="С" />
        <SearchingField v-model="dateTo" type="date" placeholder="По (включительно)" label="По" />
        <div class="w-full flex justify-center">
            <button @click="applyFilters" class="baseBtn w-32">Применить</button>
        </div>

        <button @click="$emit('clicked')" class="absolute top-2 right-2 text-gray-500 hover:text-black">
            ✕
        </button>
    </div>
</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import SearchingField from '@/components/SearchingField.vue';

const title = ref('')
const dateFrom = ref('')
const dateTo = ref('')

const emit = defineEmits(['update:filters', "clicked"])

function applyFilters() {
    emit('update:filters', {
        title: title.value || null,
        date_from: dateFrom.value || null,
        date_to: dateTo.value || null
    })
}
</script>
