<template>
  <div class="space-y-2 flex items-center gap-x-2">
    <p class="text-base text-[#5A3A2B] -mb-1">{{ label + ":" }}</p>
    <span class="ml-1 text-[#272322]">{{ value }}</span>
  </div>
</template>

<script setup lang="ts">

defineProps<{
  label: string
  value: string
}>()

</script>
