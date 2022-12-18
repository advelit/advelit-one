import { registerPlugin } from '@capacitor/core';

import type { AdvelitOnePlugin } from './definitions.js';

const AdvelitOne = registerPlugin<AdvelitOnePlugin>('AdvelitOne', {
  web: () => import('./web.js').then(m => new m.AdvelitOneWeb()),
});

export * from './definitions.js';
export { AdvelitOne };
