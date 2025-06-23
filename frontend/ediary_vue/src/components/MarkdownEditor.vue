<template>

  <div class="h-screen w-full flex flex-col justify-between items-center pt-2 bg-fire">
    <input type="text" v-model="title" class="baseBtn text-center font-bold">
    <div class="h-screen w-full flex justify-between content-center items-center gap-2 px-2">
      <button class="sideBtnL" @click="gotoMenu"> Назад </button>
      <div class="w-[90vw] h-full flex items-center opacity-95">
        <v-md-editor v-model="content" height="90vh" class="w-full"/>
      </div>
      <div class="h-[90vh] flex flex-col gap-y-1">
        <button @click="save" class="sideBtnR"> Сохранить </button>
        <button @click="remove" class="sideBtnR"> Удалить </button>
      </div>
    </div>
  </div>
  
</template>

<script setup lang="ts">
import { ref, onMounted, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router'

import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import ruRU from '@kangc/v-md-editor/lib/lang/ru-RU';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';

import Prism from 'prismjs';
import { api } from '@/api/axios';

import type { EntryInfoDTO, EntryUpdateDTO } from '@/types/Entry';

VMdEditor.use(githubTheme, { Prism });
VMdEditor.lang.use('ru-RU', ruRU)

const route = useRoute()
const router = useRouter()
const entryId = route.params.id

const content: Ref<string> = ref('')
const title: Ref<string> = ref('')

onMounted(async () => {
  const entryInfo = await api.get<EntryInfoDTO>(`/entries/${entryId}`)
  content.value = `${entryInfo.data.content}`;
  title.value = `${entryInfo.data.title}`;
})

function save() {
  const updatedEntry: EntryUpdateDTO = { title: title.value, content: content.value}
  api.patch<EntryUpdateDTO>(`/entries/${entryId}`, updatedEntry)
}

function remove(): void {
  api.delete(`/entries/${entryId}`)
  gotoMenu();
}

const gotoMenu = () => {router.push('/menu')}
</script>
