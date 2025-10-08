(async () => {
  const metricsUrl = '/status/metrics';
  const el = id => document.getElementById(id);

  function parseStubStatus(text){
    const lines = text.trim().split('\n').map(l=>l.trim());
    const res = {};
    for(const ln of lines){
      if(ln.startsWith('Active connections:')){
        res.active = parseInt(ln.split(':')[1].trim(),10);
      } else if(/^\d+\s+\d+\s+\d+$/.test(ln)){
        const parts = ln.split(/\s+/).map(Number);
        res.accepts = parts[0];
        res.handled = parts[1];
        res.requests = parts[2];
      } else if(ln.startsWith('Reading:')){
        const m = ln.match(/Reading:\s*(\d+)\s*Writing:\s*(\d+)\s*Waiting:\s*(\d+)/i);
        if(m){
          res.reading = parseInt(m[1],10);
          res.writing = parseInt(m[2],10);
          res.waiting = parseInt(m[3],10);
        }
      }
    }
    return res;
  }

  async function fetchAndRender(){
    try {
      const r = await fetch(metricsUrl, {cache: 'no-store'});
      if(!r.ok) throw new Error('HTTP ' + r.status);
      const text = await r.text();
      const m = parseStubStatus(text);

      el('active').textContent = m.active ?? '—';
      el('accepts').textContent = m.accepts ?? '—';
      el('handled').textContent = m.handled ?? '—';
      el('requests').textContent = m.requests ?? '—';
      el('reading').textContent = m.reading ?? '—';
      el('writing').textContent = m.writing ?? '—';
      el('waiting').textContent = m.waiting ?? '—';

      el('last').textContent = 'Последнее: ' + (new Date()).toLocaleTimeString();
    } catch (err) {
      el('last').textContent = 'Ошибка: ' + err.message;
    }
  }

  // UI controls
  const refreshBtn = el('refreshBtn');
  const autoCheck = el('autoRefresh');
  const intervalSel = el('interval');

  refreshBtn.addEventListener('click', fetchAndRender);

  let timer = null;
  function startAuto(){
    stopAuto();
    const ms = parseInt(intervalSel.value, 10) || 5000;
    timer = setInterval(fetchAndRender, ms);
  }
  function stopAuto(){ if(timer) { clearInterval(timer); timer = null; } }

  autoCheck.addEventListener('change', () => {
    if(autoCheck.checked) startAuto(); else stopAuto();
  });
  intervalSel.addEventListener('change', () => {
    if(autoCheck.checked) startAuto();
  });

  // initial
  await fetchAndRender();
  if(autoCheck.checked) startAuto();

})();
