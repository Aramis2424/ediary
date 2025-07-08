<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { createMood } from '@/services/moodService'
import SurveyBaseInput from '@/components/SurveyBaseInput.vue'
import SurveySelectInput from '@/components/SurveySelectInput.vue'

const emit = defineEmits(['clicked'])
const owner = useAuthStore()

const dayRating = ref<number | null>(null)
const moodRating = ref<number | null>(null)
const sleepTime = ref('')
const wakeTime = ref('')

const handleSubmit = async () => {
  if (dayRating.value === null || moodRating.value === null) {
    alert('Пожалуйста, укажите оценки дня и настроения')
    return
  }
  if (sleepTime.value === '' || wakeTime.value === '') {
    alert('Пожалуйста, укажите время сна и пробуждения')
    return
  }
  
  if (!owner.user)
    throw new Error("Error while creating mood")
  
  try {
    await createMood(
      owner.user.id, 
      dayRating.value, 
      moodRating.value, 
      sleepTime.value, 
      wakeTime.value
    )    
    emit('clicked')
  } catch {
    console.error("Error while creating entry");
  }
}
</script>

<template>
  <div class="moodBacking flex justify-center items-center">
    <div class="max-w-lg w-full mx-auto p-6 bg-white rounded-2xl 
                shadow space-y-6 flex flex-col content-start">
      <h1 class="text-2xl font-semibold text-center">Опросник дня</h1>

      <SurveySelectInput label="1. Как прошёл ваш день?" v-model="dayRating" />
      <SurveySelectInput label="2. Какое у вас настроение?" v-model="moodRating" />
      <SurveyBaseInput v-model="sleepTime" type="time" label="3. Во сколько вы уснули вчера?" />
      <SurveyBaseInput v-model="wakeTime" type="time" label="4. Во сколько Вы проснулись сегодня?" />

      <div class="text-center">
        <button @click="handleSubmit" class="px-6 py-2 baseBtn" >
          Отправить
        </button>
      </div>

    </div>
  </div>
</template>
