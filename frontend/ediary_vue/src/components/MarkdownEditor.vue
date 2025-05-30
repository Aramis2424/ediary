<template>

  <!-- <div class="h-screen flex">
    <div class="w-[90vw] h-full mx-auto">
      <MarkdownEditor class="w-full h-full" />
    </div>
  </div> -->
  <v-md-editor v-model="content" />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import ruRU from '@kangc/v-md-editor/lib/lang/ru-RU';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';

import Prism from 'prismjs';

VMdEditor.use(githubTheme, { Prism });
VMdEditor.lang.use('ru-RU', ruRU)

const content = ref<string>('Ваши *мысли* ...');
</script>
