<template>

<div>
    <label class="block font-normal mb-1">{{ label }} </label>
    <input
      v-model="modelValueProxy"
      :type="type"
      class="inputSurveyField"
    />
</div>

</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  modelValue: string
  type: string
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
