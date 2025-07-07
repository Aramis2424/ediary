<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 bg-lemon">
    <div class="w-full max-w-md bg-white p-8 rounded-2xl text-[#5A3A2B] font-semibold shadow opacity-95">
      <h2 class="text-3xl text-center mb-3"> Вход </h2>
      <form @submit.prevent="handleLogin" class="space-y-8">
        <div class="space-y-3">
            <FormField v-model="login" type="text" placeholder="Введите Ваш логин" label="Логин" />
            <FormField v-model="password" type="passwaord" placeholder="Введите Ваш пароль" label="Пароль" />
        </div>
        <button
          type="submit"
          class="baseBtn p-3 w-full text-xl"
        >
          Войти
        </button>
      </form>
      <p class="text-base text-center">
        Нет аккаунта?
        <router-link to="/register" class="text-[#E67E22] font-bold hover:underline"> Регистрация </router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import FormField from '@/components/FormField.vue';

const router = useRouter();
const auth = useAuthStore();

const login = ref('');
const password = ref('');

async function handleLogin() {
  try {
    await auth.login({ login: login.value, password: password.value })
    router.push('/home');
  } catch {
    alert('Неверные данные')
  }
}
</script>
