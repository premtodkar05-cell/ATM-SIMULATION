# 🚀 Deploying ATM Simulation to Vercel

This project includes a **production-ready Web Edition** ([`index.html`](file:///c:/Users/Premtodkar/Downloads/ATM/ATMSimulation/index.html) and [`vercel.json`](file:///c:/Users/Premtodkar/Downloads/ATM/ATMSimulation/vercel.json)) pre-configured for instant zero-configuration deployment on **Vercel**.

---

## ⚡ Method 1: Deploy with GitHub (Recommended & Easiest)

1. **Push your code to GitHub**:
   - Create a new repository on [github.com](https://github.com/new) named `atm-simulation`.
   - Push this `ATMSimulation` folder to GitHub.

2. **Import into Vercel**:
   - Go to [vercel.com/new](https://vercel.com/new).
   - Sign in with your GitHub account.
   - Click **"Import"** next to your `atm-simulation` repository.
   - Leave the Framework Preset as **Other** (Root directory: `./` where `index.html` resides).
   - Click **"Deploy"**.

3. **Done!**
   - In less than 15 seconds, Vercel gives you a public URL like:
     ```
     https://atm-simulation-xxx.vercel.app
     ```
   - Share this live link directly with teachers, examiners, or friends!

---

## ⚡ Method 2: Deploy via Vercel CLI

If you have Node.js installed, open terminal in this directory:
```bash
npx vercel
```
1. Follow the prompts (Select `Y` to link project, keep defaults).
2. For production deployment:
```bash
npx vercel --prod
```

---

## 🌟 Key Features of the Vercel Online Edition

- **Interactive CRT ATM Terminal**: Realistic banking screen with 6 action buttons.
- **Physical ATM Hardware Keypad**: Working 0-9 keys, Clear, and Enter with Web Audio sound synthesizer (no external audio assets needed).
- **Cash Dispenser Tray**: Animated note dispensing for withdrawals.
- **Printable Thermal ATM Slip**: Realistic bank receipt popup with printable dialog.
- **Local Storage Persistence**: Automatically persists balance and transaction history in browser storage across restarts and refreshes.
- **Full Mobile & Desktop Support**: Responsive layout tested for phones, tablets, and monitors.
