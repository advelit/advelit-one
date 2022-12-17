import { WebPlugin } from '@capacitor/core';

import type { AdvelitOnePlugin } from './definitions';

export class AdvelitOneWeb extends WebPlugin implements AdvelitOnePlugin {
    async removeLauncher(): Promise<any> {
        return null;
    };

    async reboot(): Promise<any> {
        return null;
    };

    async ethernetUp(): Promise<any> {
        return true;
    };

    async ethernetDown(): Promise<any> {
        return true;
    };

    async toggleAutoStart(options: { enabled: boolean, serviceClassName?: string }): Promise<any> {
        return {
            ...options,
            status: true,
        };
    };

    async getInstalledApps(): Promise<any> {
        return [];
    };
}
