import { registerPlugin } from '@capacitor/core';

import type { AdvelitOnePlugin } from './definitions';

const AdvelitOne = registerPlugin<AdvelitOnePlugin>('AdvelitOne', {
  web: () => import('./web').then(m => new m.AdvelitOneWeb()),
});

export * from './definitions';
export { AdvelitOne };
