<template>

  <div class="h-[100vh] w-4/5 flex flex-col justify-center items-center gap-2 bg-fire">
    <div class="flex mt-1 -mb-1">
      <input type="text" v-model="title" class="baseTitle text-center font-bold text-3xl">
    </div>
    <div class="h-screen w-full flex justify-between content-center items-center gap-0">
      <MdEditor v-model:content="content" />
    </div>
    <div class="flex justify-center gap-2 -mt-1 w-full">
      <button @click="save" class="baseBtn w-1/5"> Сохранить </button>
      <button @click="remove" class="baseBtn w-1/5"> Удалить </button>
    </div>
  </div>

</template>

<script setup lang="ts">
import { ref, onMounted, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router'

import type { EntryInfoDTO, EntryUpdateDTO } from '@/types/Entry';
import { fetchEntry, removeEntry, updateEntry } from '@/services/entryService';

import MdEditor from '@/components/MdEditor.vue';
import { useToast } from 'vue-toastification'

const toast = useToast()
const route = useRoute()
const router = useRouter()
const entryId = route.params.id

const content: Ref<string> = ref('')
const title: Ref<string> = ref('')

onMounted(async () => {
  await fetch();
})

async function fetch(): Promise<void> {
  try {
    const entryInfo: EntryInfoDTO = await fetchEntry(Number(entryId));
    content.value = `${entryInfo.content}`;
    title.value = `${entryInfo.title}`;
  } catch{
    console.error("Error while fetching entry");
  }
}

async function save(): Promise<void> {
  const updatedEntry: EntryUpdateDTO = { title: title.value, content: content.value}
  try {
    await updateEntry(Number(entryId), updatedEntry);
    toast.success('Сохранено')
  } catch {
    console.error("Error while removing entry");
    toast.error('Ошибка. попробуйте снова')
  }
}

async function remove(): Promise<void> {
  try {
    await removeEntry(Number(entryId));
    gotoMenu();
  } catch{
    console.error("Error while removing entry");
  }
}

const gotoMenu = () => {router.push('/menu')}
</script>
