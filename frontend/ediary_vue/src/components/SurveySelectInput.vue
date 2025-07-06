<template>
<div>
  <label class="block font-normal mb-1">{{ label }} </label>
  <select v-model.number="modelValueProxy" class="inputSurveyField">
    <option disabled value="">Выберите оценку</option>
    <option v-for="n in 10" :key="n" :value="n">{{ n }}</option>
  </select>
</div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  modelValue: number | null
  label: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const modelValueProxy = computed({
  get: () => props.modelValue,
  set: (val: string) => emit('update:modelValue', val),
})
</script>
