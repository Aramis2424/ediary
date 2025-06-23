<script setup lang="ts">
defineEmits(['clicked'])
</script>

<template>
  <div @click="$emit('clicked')" class="flex justify-center items-center baseBtn w-1/5 h-1/5 min-w-72 min-h-36">
    <div class="relative w-[3vw] h-[3vw] rounded-full bg-[#F4A261] opacity-90 ">
      <div class="absolute top-1/2 left-1/2 w-[10%] h-[70%] bg-white -translate-x-1/2 -translate-y-1/2 rounded"></div>
      <div class="absolute top-1/2 left-1/2 w-[70%] h-[10%] bg-white -translate-x-1/2 -translate-y-1/2 rounded"></div>
    </div>
  </div>
</template>

<style scoped>
</style>
