<script setup lang="ts">

const props = defineProps<{
  label: string
  modelValue: number | null
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: number): void
}>()

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
}

const handleSelect = (value: number) => {
  emit('update:modelValue', value)
}

const isSelected = (n: number) => props.modelValue === n
</script>

<template>
  <div class="flex flex-col gap-3">
    <label class="text-base font-medium text-gray-800">
      {{ label }}
    </label>

    <div class="flex gap-2">
      <button
        v-for="n in 10"
        :key="n"
        @click="handleSelect(n)"
        :class="[
          'w-10 h-10 flex items-center justify-center rounded-full border text-sm font-semibold transition-all duration-150',
          isSelected(n)
            ? `${scoreColors[n]} text-white border-transparent scale-110 shadow-md`
            : 'baseBtn'
        ]"
      >
        {{ n }}
      </button>
    </div>
  </div>
</template>

<style scoped>
button {
  transition: all 0.15s ease-in-out;
}
</style>
