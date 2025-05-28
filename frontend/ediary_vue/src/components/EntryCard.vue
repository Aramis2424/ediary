<script setup lang="ts">
import { computed, type ComputedRef } from 'vue';

function getDayOfWeek(dateString: string) {
    const daysOfWeek = [
    'Воскресенье',
    'Понедельник',
    'Вторник',
    'Среда',
    'Четверг',
    'Пятница',
    'Суббота'
    ];
    const date = new Date(dateString);
    if (!date) {
        return 'Error';
    }

    const dayIndex = date.getDay();
    return daysOfWeek[dayIndex];
}

const scoreColors: Record<number, string> = {
  1: 'bg-[#A50026]',
  2: 'bg-[#D73027]',
  3: 'bg-[#F46D43]',
  4: 'bg-[#FDAE61]',
  5: 'bg-[#FEE08B]',
  6: 'bg-[#D9EF8B]',
  7: 'bg-[#A6D96A]',
  8: 'bg-[#66BD63]',
  9: 'bg-[#1A9850]',
  10: 'bg-[#006837]',
};


const props = defineProps<{ 
    title: string,
    date: string,
    mood: number,
    work: number
 }>()

const moodColor: ComputedRef<string> = computed(() => {
  return scoreColors[props.mood] || 'bg-gray-100';
});
const workColor: ComputedRef<string> = computed(() => {
  return scoreColors[props.work] || 'bg-gray-100';
});

</script>

<template>
  <div class="relative baseBtn w-1/5 h-1/5 min-w-72 min-h-36">
    <p class="absolute top-0 left-1/2 -translate-x-1/2 text-center font-czizh text-3xl underline"> {{ title }} </p>

    <div class="absolute top-1/2 right-0 -translate-x-3 translate-y-px">
        <p class="text-lg -mb-1"> {{ date }} </p>
        <p class="text-lg"> {{ getDayOfWeek(date) }} </p>
    </div>

    <div class="absolute top-2/3 pl-3">
        <div class="flex items-center space-x-2">
            <div :class="['w-4 h-4 rounded-full', moodColor]"></div>
            <span class="text-sm font-medium">{{ 'настроние: ' + mood }}</span>
        </div>
        <div class="flex items-center space-x-2">
            <div :class="['w-4 h-4 rounded-full', workColor]"></div>
            <span class="text-sm font-medium">{{ 'день: ' + work }}</span>
        </div>
    </div>
  </div>
</template>

<style scoped>
</style>
