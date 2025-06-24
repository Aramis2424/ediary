<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useOwnerStore } from '../stores/owner';
import { onMounted, ref, type Ref } from 'vue';
import axios from 'axios';
import type { OwnerInfoDTO } from '@/types/Owner';
import Settings from './Settings.vue';

const showSettings = ref(false);
const router = useRouter();
const owner = useOwnerStore();

const gotoEntriesMenu = () => {router.push('/menu')}
const gotoMoodGraph = () => {router.push('/graph')}

const ownerName: Ref<string> = ref('');
onMounted(async () => {
  const ownerInfo = await axios.get<OwnerInfoDTO>(`/api/owners/${owner.id}`)
  ownerName.value = ownerInfo.data.name
})
</script>

<template>
   <div class="h-screen overflow-auto flex flex-col items-center justify-between p-2 relative bg-lemon space-y-10">
    <h1 class="font-czizh text-white bg-[#B4B4B4] bg-opacity-10 text-6xl absolute top-4 left-1/2 -translate-x-1/2">
      Ваши мысли, {{ ownerName }}!
    </h1>
    <div class="flex flex-1 w-full items-center justify-between">
      <button class="sideBtnL"> О себе </button>

      <div class="flex flex-col items-center justify-center gap-4 h-full w-full max-w-md">
        <button @click="gotoEntriesMenu()" class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
          Личный дневник
        </button>
        <button @click="gotoMoodGraph()" class="w-[30vw] h-16 baseBtn text-black text-2xl md:text-3xl lg:text-4xl font-czizh">
          Настроения
        </button>
      </div>

      <button @click="showSettings = true" class="sideBtnR" style="letter-spacing: 2px;"> Настройки </button>
    </div>
  </div>

  <Settings v-if="showSettings" @clicked="showSettings = false"/> 

</template>

<style scoped>
</style>
