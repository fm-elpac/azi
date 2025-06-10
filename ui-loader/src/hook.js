import { onMounted, ref } from "vue";

// azi_api
function getApi() {
  return window.azi_api;
}

// azi_api.getJsLoadList()
function getJsLoadList() {
  const a = getApi();
  if ((null != a) && (typeof a.getJsLoadList == "function")) {
    return a.getJsLoadList();
  }
  return [];
}

// azi_api.checkInit()
function checkInit() {
  const a = getApi();
  if ((null != a) && (typeof a.checkInit == "function")) {
    return a.checkInit();
  }
  return null;
}

function loadJs(list) {
  // DEBUG
  console.log("jsLoadList", list);

  // TODO
}

export function useCheck() {
  const text = ref(null);

  function updateText() {
    text.value = checkInit();
  }

  onMounted(() => {
    setInterval(updateText, 1e3);
    updateText();

    const list = getJsLoadList();
    loadJs(list);
  });

  return { text };
}
