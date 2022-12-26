export interface AdvelitOnePlugin {
    removeLauncher(): Promise<any>;

    reboot(): Promise<any>;

    ethernetUp(): Promise<any>;

    ethernetDown(): Promise<any>;

    isDebuggingOpen(): Promise<any>;

    openDebugging(): Promise<any>;

    closeDebugging(): Promise<any>;

    getCPUTemperature(): Promise<any>;

    toggleAutoStart(options: { enabled: boolean, serviceClassName?: string }): Promise<any>;

    runCommand(options: { command: string }): Promise<any>;

    getInstalledApps(): Promise<any>;
}
