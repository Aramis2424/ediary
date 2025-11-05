<template>
  <div class="space-y-2 flex items-center gap-x-2">
    <p class="text-base text-[#5A3A2B] -mb-1">{{ label + ":" }}</p>
    <!-- <input
      v-model="modelValueProxy"
      :type="type"
      :placeholder="placeholder"
      class="w-full h-[4vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
    /> -->
    <!-- <span class="font-semibold text-gray-900">{{ label }}</span> -->
    <span class="ml-1 text-[#272322]">{{ value }}</span>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  label: string
  value: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const modelValueProxy = computed({
  get: () => props.value,
  set: (val: string) => emit('update:modelValue', val),
})
</script>
