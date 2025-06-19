<template>

  <div class="h-screen flex justify-center content-center items-center bg-fire">
    <div class="w-[90vw] h-full flex items-center opacity-95">
      <v-md-editor v-model="content" height="90vh" class="w-full"/>
    </div>
  </div>
  
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRoute } from 'vue-router'

import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import ruRU from '@kangc/v-md-editor/lib/lang/ru-RU';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';

import Prism from 'prismjs';

VMdEditor.use(githubTheme, { Prism });
VMdEditor.lang.use('ru-RU', ruRU)

const route = useRoute()
const entryId = route.params.id

const content = ref<string>(`Ваши *мысли* ... (${entryId})`);
</script>
