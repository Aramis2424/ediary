<template>
<div class="baseBacking">
    <div class="bg-white rounded-xl p-2 w-1/6 min-h-1/6 shadow-xl relative flex flex-col justify-evenly content-start items-start">
        <h2 class="text-xl font-semibold mb-4">О себе</h2>
        <p> Имя: {{ ownerName }} </p>
        <p> Дата рождения: {{ ownerBirthdate }} </p>
        <p> Логин: {{ ownerLogin }} </p>
        <p> Зарегистрирован: {{ ownerCreateDate }} </p>
        <button @click="$emit('clicked')" class="absolute top-2 right-2 text-gray-500 hover:text-black">
            ✕
        </button>
    </div>
</div>
</template>

<script setup lang="ts">
import { ref, type Ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const emit = defineEmits(["clicked"])

const owner = useAuthStore().user;
const ownerName: Ref<string> = ref(owner?.name ?? "");
const ownerBirthdate: Ref<string> = ref(owner?.birthDate ?? "");
const ownerLogin: Ref<string> = ref(owner?.login ?? "");
const ownerCreateDate: Ref<string> = ref(owner?.createdDate ?? "");

</script>
