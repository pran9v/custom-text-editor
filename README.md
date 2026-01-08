# Custom Java Text Editor (Built from Scratch)

A lightweight **custom text editor implemented from scratch in Java Swing**, without using `JTextArea`, `JEditorPane`, or any built-in text components.

Project focuses on **understanding how real text editors work internally** [caret movement, text layout, selection rendering, mouse hit-testing, and multiline editing] by implementing everything manually.

---

## Features

- **Multiline text editing**
- **Custom caret rendering** with blink timer
- **Mouse interaction**: Caret placement and drag selection
- **Smart selection**: Double-click (word) and Triple-click (line)
- **Keyboard navigation**: Full arrow key and Shift + selection support
- **Line Logic**: Backspace with line merging and selection deletion across lines
- **Custom UI**: Selection and current-line highlighting
- **Fully resizable** editor panel

*No Swing text components were used — everything is rendered manually using `Graphics`.*

---

## High-Level working

- **Storage**: Text is stored as a `List<StringBuilder> lines`.
- **State Tracking**: Caret and selection are tracked using `caretRow`, `caretCol`, `anchorRow`, and `anchorCol`.
- **Rendering**: Done within `paintComponent(Graphics g)` in this order:
    1. Current line highlight
    2. Selection background
    3. Text rendering via `FontMetrics`
    4. Caret rendering
- **Hit-Testing**: Mouse clicks are mapped from pixel coordinates back to `(row, col)` using `FontMetrics`.

---

## Interaction Model

### Mouse
- **Click**: Move caret
- **Drag**: Select text
- **Double-click**: Select word
- **Triple-click**: Select entire line

### Keyboard
- **Arrow keys**: Caret navigation
- **Shift + Arrows**: Expand/contract selection
- **Backspace**: Delete character / merge lines
- **Typing**: Inserts text at caret
- **Enter**: Creates new line

---

## Limitations (By Design)
These are intentional to keep the focus on core editor mechanics:
- No Undo / Redo (yet)
- No Clipboard integration (Copy/Paste)
- No Syntax highlighting
- No Word wrapping

---

## Tech Stack
- **Language**: Java
- **Framework**: Swing (JFrame/JPanel only)
- **API**: AWT (`Graphics`, `FontMetrics`)
- **Dependencies**: None

---

## Demo



https://github.com/user-attachments/assets/a841ac8b-d8d3-484e-a55b-76195cd7a646


---

## 🚀 Future Improvements
- [ ] Clipboard (Copy / Cut / Paste)
- [ ] Undo / Redo command stack
- [ ] Scrolling optimization
- [ ] Line numbers / Gutter
- [ ] Dark mode

---

**Created by Pranav.**
