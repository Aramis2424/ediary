<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 bg-lemon">
    <div class="w-full max-w-md bg-white p-2 mt-1 mb-2 rounded-2xl text-[#5A3A2B] font-semibold shadow opacity-95">
      <h2 class="text-3xl text-center mb-3"> Регистрация </h2>
      <form @submit.prevent="handleRegister" class="space-y-6">
        <div class="space-y-3">
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Имя </p>
                <input
                v-model="name"
                type="text"
                placeholder="Введите Ваше имя"
                class="w-full h-[7vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Логин </p>
                <input
                v-model="login"
                type="text"
                placeholder="Введите Ваш логин"
                class="w-full h-[7vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Пароль </p>
                <input
                v-model="password"
                type="password"
                placeholder="Введите Ваш пароль"
                class="w-full h-[7vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Повторите пароль </p>
                <input
                v-model="confirmPassword"
                type="password"
                placeholder="Повторите Ваш пароль"
                class="w-full h-[7vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Дата рождения </p>
                <input
                v-model="confirmPassword"
                type="date"
                class="w-full h-[7vh] max-h-14 p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
        </div>
        <button
          type="submit"
          class="baseBtn p-3 w-full h-[7vh] max-h-14 text-xl text-center flex items-center justify-center"
        >
          Зарегистрироваться
        </button>
      </form>
      <p class="text-base text-center">
        Уже зарегисрированы?
        <router-link to="/login" class="text-[#E67E22] font-bold hover:underline"> Войти </router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const name = ref('');
const login = ref('');
const password = ref('');
const confirmPassword = ref('');

function handleRegister() {
  if (password.value !== confirmPassword.value) {
    alert('Пароли не совпадают!');
    return;
  }

  console.log('Регистрация:', login.value, password.value);
  router.push('/home');
  // TODO: логика регистрации
}
</script>
