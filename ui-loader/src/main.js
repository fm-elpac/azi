// azi ui-loader
import "./body.css";

import { createApp } from "vue";
import App from "./App.vue";

import vuetify from "./plugin/vuetify.js";

const app = createApp(App);
app.use(vuetify);

app.mount("#app");
