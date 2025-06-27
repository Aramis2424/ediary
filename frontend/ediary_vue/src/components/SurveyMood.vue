<script setup lang="ts">
import type { MoodCreateDTO } from '@/types/Mood'
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { api } from '@/api/axios'

const owner = useAuthStore()

const dayRating = ref<number | null>(null)
const moodRating = ref<number | null>(null)
const sleepTime = ref('')
const wakeTime = ref('')

const handleSubmit = () => {
  if (dayRating.value === null || moodRating.value === null) {
    alert('Пожалуйста, укажите оценки дня и настроения')
    return
  }
  if (sleepTime.value === null || wakeTime.value === null) {
    alert('Пожалуйста, укажите время сна и пробуждения')
    return
  }

  const result: MoodCreateDTO = {
    ownerId: owner.user?.id ?? 0,
    scoreProductivity: dayRating.value,
    scoreMood: moodRating.value,
    bedtime: sleepTime.value,
    wakeUpTime: wakeTime.value,
  }
  api.post('moods/', result);
}
</script>

<template>
  <div class="moodBacking flex justify-center items-center">
    <div class="max-w-lg w-full mx-auto p-6 bg-white rounded-2xl 
                shadow space-y-6 flex flex-col content-start">
      <h1 class="text-2xl font-semibold text-center">Опросник дня</h1>

      <!-- Вопрос 1 -->
      <div>
        <label class="block font-normal mb-1">1. Как прошёл ваш день?</label>
        <select v-model.number="dayRating" class="inputField">
          <option disabled value="">Выберите оценку</option>
          <option v-for="n in 10" :key="n" :value="n">{{ n }}</option>
        </select>
      </div>

      <!-- Вопрос 2 -->
      <div>
        <label class="block font-normal mb-1">2. Какое у вас настроение?</label>
        <select v-model.number="moodRating" class="inputField">
          <option disabled value="">Выберите оценку</option>
          <option v-for="n in 10" :key="n" :value="n">{{ n }}</option>
        </select>
      </div>

      <!-- Вопрос 3 -->
      <div>
        <label class="block font-normal mb-1">3. Во сколько вы уснули вчера?</label>
        <input v-model="sleepTime" type="time" class="inputField" />
      </div>

      <!-- Вопрос 4 -->
      <div>
        <label class="block font-normal mb-1">4. Во сколько проснулись сегодня?</label>
        <input v-model="wakeTime" type="time" class="inputField" />
      </div>

      <!-- Кнопка -->
      <div class="text-center">
        <button
          @click="handleSubmit"
          class="px-6 py-2 baseBtn"
        >
          Отправить
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.inputField {
  @apply
  bg-[#FFF8F0] bg-opacity-60 hover:bg-[#fcf3e9]
  border-[#F4A261] border-4
  rounded-3xl transition px-3
  w-1/2 
}
</style>
