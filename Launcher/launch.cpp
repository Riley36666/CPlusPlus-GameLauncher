#include <windows.h>
#include <string>
#include <fstream>
#include <filesystem>
#include <dwmapi.h>
#pragma comment(lib, "dwmapi.lib")

// forward declarations
LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
LRESULT CALLBACK WIPWindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);
void LaunchEmbeddedJar(const char* resourceName);
void OpenWIPWindow(HINSTANCE hInstance);
void DrawGradientBackground(HDC hdc, RECT rc, COLORREF top, COLORREF bottom);

// IDs for buttons
#define ID_BUTTON_PLATFORMER 1001
#define ID_BUTTON_WIP        1002

HINSTANCE g_hInstance;

// =========================
//      MAIN ENTRY POINT
// =========================
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE, LPSTR, int nCmdShow) {
    g_hInstance = hInstance;
    const char CLASS_NAME[] = "LauncherWindowClass";

    WNDCLASSA wc = {};
    wc.lpfnWndProc = WindowProc;
    wc.hInstance = hInstance;
    wc.lpszClassName = CLASS_NAME;
    wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
    wc.hCursor = LoadCursor(NULL, IDC_ARROW);

    RegisterClassA(&wc);

    HWND hwnd = CreateWindowExA(
        WS_EX_APPWINDOW,
        CLASS_NAME,
        "My Java Game Launcher",
        WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX,
        CW_USEDEFAULT, CW_USEDEFAULT, 700, 700,
        NULL, NULL, hInstance, NULL
    );

    if (!hwnd) return 0;
    ShowWindow(hwnd, nCmdShow);
    UpdateWindow(hwnd);

    MSG msg = {};
    while (GetMessageA(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessageA(&msg);
    }
    return 0;
}

// =========================
//      GRADIENT BG
// =========================
void DrawGradientBackground(HDC hdc, RECT rc, COLORREF top, COLORREF bottom) {
    TRIVERTEX vertex[2];
    vertex[0].x = rc.left;
    vertex[0].y = rc.top;
    vertex[0].Red   = GetRValue(top) << 8;
    vertex[0].Green = GetGValue(top) << 8;
    vertex[0].Blue  = GetBValue(top) << 8;
    vertex[0].Alpha = 0x0000;

    vertex[1].x = rc.right;
    vertex[1].y = rc.bottom;
    vertex[1].Red   = GetRValue(bottom) << 8;
    vertex[1].Green = GetGValue(bottom) << 8;
    vertex[1].Blue  = GetBValue(bottom) << 8;
    vertex[1].Alpha = 0x0000;

    GRADIENT_RECT gRect = { 0, 1 };
    GradientFill(hdc, vertex, 2, &gRect, 1, GRADIENT_FILL_RECT_V);
}

// =========================
//      MAIN WINDOW PROC
// =========================
LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    static HFONT hFontTitle, hFontButton;
    static HWND hTitle, hPlatformer, hWIP;

    switch (uMsg) {
    case WM_CREATE:
        hFontTitle = CreateFontA(40, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE,
                                 DEFAULT_CHARSET, OUT_OUTLINE_PRECIS, CLIP_DEFAULT_PRECIS,
                                 CLEARTYPE_QUALITY, VARIABLE_PITCH, "Segoe UI");

        hFontButton = CreateFontA(22, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE,
                                  DEFAULT_CHARSET, OUT_OUTLINE_PRECIS, CLIP_DEFAULT_PRECIS,
                                  CLEARTYPE_QUALITY, VARIABLE_PITCH, "Segoe UI");

        hTitle = CreateWindowA("static", "My Game Launcher",
                               WS_VISIBLE | WS_CHILD | SS_CENTER,
                               200, 100, 300, 60,
                               hwnd, NULL, g_hInstance, NULL);

        hPlatformer = CreateWindowA("button", "Play Platformer",
                                    WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON,
                                    260, 250, 180, 50,
                                    hwnd, (HMENU)ID_BUTTON_PLATFORMER, g_hInstance, NULL);

        hWIP = CreateWindowA("button", "WIP (Coming Soon)",
                             WS_VISIBLE | WS_CHILD | BS_PUSHBUTTON,
                             260, 340, 180, 50,
                             hwnd, (HMENU)ID_BUTTON_WIP, g_hInstance, NULL);

        SendMessageA(hTitle, WM_SETFONT, (WPARAM)hFontTitle, TRUE);
        SendMessageA(hPlatformer, WM_SETFONT, (WPARAM)hFontButton, TRUE);
        SendMessageA(hWIP, WM_SETFONT, (WPARAM)hFontButton, TRUE);
        return 0;

    case WM_CTLCOLORBTN:
    case WM_CTLCOLORSTATIC: {
        HDC hdc = (HDC)wParam;
        SetBkMode(hdc, TRANSPARENT);
        SetTextColor(hdc, RGB(255, 255, 255));
        return (LRESULT)GetStockObject(NULL_BRUSH);
    }

    case WM_PAINT: {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hwnd, &ps);
        RECT rc;
        GetClientRect(hwnd, &rc);
        DrawGradientBackground(hdc, rc, RGB(10, 10, 30), RGB(30, 30, 80));
        EndPaint(hwnd, &ps);
        return 0;
    }

    case WM_COMMAND:
        switch (LOWORD(wParam)) {
        case ID_BUTTON_PLATFORMER:
            DestroyWindow(hwnd);
            LaunchEmbeddedJar("gamejar");
            break;

        case ID_BUTTON_WIP:
            DestroyWindow(hwnd);
            OpenWIPWindow(g_hInstance);
            break;
        }
        return 0;

    case WM_DESTROY:
        PostQuitMessage(0);
        return 0;
    }

    return DefWindowProcA(hwnd, uMsg, wParam, lParam);
}

// =========================
//     WIP PLACEHOLDER
// =========================
void OpenWIPWindow(HINSTANCE hInstance) {
    const char WIP_CLASS[] = "WIPWindowClass";
    WNDCLASSA wc = {};
    wc.lpfnWndProc = WIPWindowProc;
    wc.hInstance = hInstance;
    wc.lpszClassName = WIP_CLASS;
    RegisterClassA(&wc);

    HWND hwnd = CreateWindowExA(
        0,
        WIP_CLASS,
        "Work in Progress",
        WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU,
        CW_USEDEFAULT, CW_USEDEFAULT, 500, 400,
        NULL, NULL, hInstance, NULL
    );

    ShowWindow(hwnd, SW_SHOW);
    UpdateWindow(hwnd);
}

LRESULT CALLBACK WIPWindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    switch (uMsg) {
    case WM_PAINT: {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hwnd, &ps);
        RECT rc;
        GetClientRect(hwnd, &rc);
        DrawGradientBackground(hdc, rc, RGB(50, 20, 20), RGB(100, 40, 40));

        SetBkMode(hdc, TRANSPARENT);
        SetTextColor(hdc, RGB(255, 255, 255));

        HFONT font = CreateFontA(36, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE,
                                 DEFAULT_CHARSET, OUT_OUTLINE_PRECIS, CLIP_DEFAULT_PRECIS,
                                 CLEARTYPE_QUALITY, VARIABLE_PITCH, "Segoe UI");
        SelectObject(hdc, font);
        DrawTextA(hdc, "🚧 Work in Progress 🚧", -1, &rc, DT_CENTER | DT_VCENTER | DT_SINGLELINE);
        DeleteObject(font);
        EndPaint(hwnd, &ps);
        return 0;
    }
    case WM_DESTROY:
        PostQuitMessage(0);
        return 0;
    }
    return DefWindowProcA(hwnd, uMsg, wParam, lParam);
}

// =========================
//     JAR LAUNCHER LOGIC
// =========================
void LaunchEmbeddedJar(const char* resourceName) {
    HRSRC hRes = FindResourceA(NULL, resourceName, RT_RCDATA);
    if (!hRes) {
        MessageBoxA(NULL, "Failed to find embedded JAR resource.", "Error", MB_ICONERROR);
        return;
    }

    HGLOBAL hData = LoadResource(NULL, hRes);
    DWORD size = SizeofResource(NULL, hRes);
    void* data = LockResource(hData);

    std::string tempPath = (std::filesystem::temp_directory_path() / "GameTemp.jar").string();
    std::ofstream out(tempPath, std::ios::binary);
    out.write(static_cast<const char*>(data), size);
    out.close();

    std::string cmd = "javaw -jar \"" + tempPath + "\"";
    STARTUPINFOA si = { sizeof(si) };
    si.dwFlags = STARTF_USESHOWWINDOW;
    si.wShowWindow = SW_HIDE;
    PROCESS_INFORMATION pi{};

    if (!CreateProcessA(NULL, cmd.data(), NULL, NULL, FALSE, CREATE_NO_WINDOW, NULL, NULL, &si, &pi)) {
        MessageBoxA(NULL, "Failed to launch Java.\nMake sure Java is installed.", "Error", MB_ICONERROR);
        return;
    }

    WaitForSingleObject(pi.hProcess, INFINITE);
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);
    std::filesystem::remove(tempPath);
}
