<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useOwnerStore } from '../stores/owner';

const router = useRouter();
const owner = useOwnerStore();

const gotoEntriesMenu = () => {router.push('/menu')}
</script>

<template>
   <div class="h-screen overflow-auto flex flex-col items-center justify-between p-2 relative bg-lemon space-y-10">
    <h1 class="font-czizh text-white bg-[#B4B4B4] bg-opacity-10 text-6xl absolute top-4 left-1/2 -translate-x-1/2">
      Ваши мысли, {{ owner.name }}!
    </h1>
    <div class="flex flex-1 w-full items-center justify-between">
      <button class="sideBtnL"> О себе </button>

      <div class="flex flex-col items-center justify-center gap-4 h-full w-full max-w-md">
        <button @click="gotoEntriesMenu()" class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
          Личный дневник
        </button>
        <button class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
          Настроения
        </button>
      </div>

      <button class="sideBtnR" style="letter-spacing: 2px;"> Настройки </button>
    </div>
  </div>
</template>

<style scoped>
</style>
