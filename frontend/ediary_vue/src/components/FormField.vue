<template>
  <div class="space-y-2">
    <p class="text-xl text-[#5A3A2B] -mb-1">{{ label }}</p>
    <input
      v-model="modelValueProxy"
      :type="type"
      :placeholder="placeholder"
      class="w-full h-[7vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  modelValue: string
  type: string
  placeholder: string
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
