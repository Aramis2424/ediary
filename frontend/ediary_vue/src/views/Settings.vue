<template>
<div class="baseBacking">
    <div class="bg-white rounded-xl p-2 w-1/6 min-h-1/6 shadow-xl relative flex flex-col justify-evenly content-center items-center gap-2">
        <h2 class="text-xl font-semibold mb-4">Настройки</h2>
        <a :href="mailtoLink" class="baseBtn w-5/6 text-center">
            Сообщить о баге
        </a>
        <button @click="logout" class="baseBtn w-5/6">
            Выйти
        </button>
        <button @click="$emit('clicked')" class="absolute top-2 right-2 text-gray-500 hover:text-black">
            ✕
        </button>
    </div>
</div>
</template>

<script setup lang="ts">
import router from '@/router';
import { useAuthStore } from '@/stores/auth';
const emit = defineEmits(["clicked"])
const auth = useAuthStore()
function logout(): void {
    auth.logout();
    router.push('/login')
    emit('clicked');
}
const mailtoLink = `mailto:korovroman2424@yandex.ru?subject=Баг%20в%20приложении%20ediary`
</script>
