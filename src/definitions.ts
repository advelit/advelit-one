export interface AdvelitOnePlugin {
    removeLauncher(): Promise<any>;

    reboot(): Promise<any>;

    ethernetUp(): Promise<any>;

    ethernetDown(): Promise<any>;

    toggleAutoStart(options: { enabled: boolean, serviceClassName?: string }): Promise<any>;

    getInstalledApps(): Promise<any>;
}
