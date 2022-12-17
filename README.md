# advelit-one

Core Service Provider for Advelit One.

## Install

```bash
npm install advelit-one
npx cap sync
```

## API

<docgen-index>

* [`removeLauncher()`](#removelauncher)
* [`reboot()`](#reboot)
* [`ethernetUp()`](#ethernetup)
* [`ethernetDown()`](#ethernetdown)
* [`toggleAutoStart(...)`](#toggleautostart)
* [`getInstalledApps()`](#getinstalledapps)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### removeLauncher()

```typescript
removeLauncher() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### reboot()

```typescript
reboot() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### ethernetUp()

```typescript
ethernetUp() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### ethernetDown()

```typescript
ethernetDown() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### toggleAutoStart(...)

```typescript
toggleAutoStart(options: { enabled: boolean; serviceClassName?: string; }) => Promise<any>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code>{ enabled: boolean; serviceClassName?: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### getInstalledApps()

```typescript
getInstalledApps() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------

</docgen-api>
