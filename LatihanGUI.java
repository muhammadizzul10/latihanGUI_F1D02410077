import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Latihan GUI Java Swing - Form Biodata Mahasiswa.
 *
 * 1. Top-level Container   : JFrame
 * 2. Intermediate Container: JPanel, JScrollPane
 * 3. Atomic Component      : JLabel, JTextField, JComboBox, JSpinner, JCheckBox, JTextArea, JButton
 * 4. Layout Classes        : BorderLayout, GridBagLayout, FlowLayout, GridLayout
 * 5. Event Listener        : ActionListener, ItemListener (interface)
 *                            MouseAdapter, KeyAdapter, WindowAdapter (adapter class)
 */
public class LatihanGUI extends JFrame {

    private static final Color BIRU = new Color(33, 84, 160);
    private static final Color BIRU_HOVER = new Color(70, 130, 210);
    private static final Color MERAH = new Color(190, 50, 50);
    private static final Color MERAH_HOVER = new Color(225, 90, 90);

    // ===== Atomic Components =====
    private final JTextField tfNama = new JTextField(18);
    private final JTextField tfNim = new JTextField(18);
    private final JComboBox<String> cbProdi = new JComboBox<>(
            new String[]{"Informatika", "Sistem Informasi", "Teknik Elektro", "Teknik Sipil"});
    private final JSpinner spSemester = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));
    private final JCheckBox ckCoding = new JCheckBox("Coding");
    private final JCheckBox ckGame = new JCheckBox("Game");
    private final JCheckBox ckMusik = new JCheckBox("Musik");
    private final JTextArea taHasil = new JTextArea(7, 30);
    private final JLabel lblStatus = new JLabel(" Siap digunakan.");
    private int jumlahData = 0;

    public LatihanGUI() {
        super("Latihan GUI - Form Biodata Mahasiswa");          // Top-level Container
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(0, 8));                       // Layout 1: BorderLayout

        add(buatHeader(), BorderLayout.NORTH);
        add(buatIsi(), BorderLayout.CENTER);
        add(buatFooter(), BorderLayout.SOUTH);
        pasangEvent();

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    // ===== Intermediate Container: header =====
    private JPanel buatHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));   // Layout 2: FlowLayout
        p.setBackground(BIRU);
        JLabel judul = new JLabel("FORM BIODATA MAHASISWA");
        judul.setFont(new Font("SansSerif", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);
        p.add(judul);
        return p;
    }

    // ===== Intermediate Container: isi (form + hasil) =====
    private JPanel buatIsi() {
        JPanel isi = new JPanel(new BorderLayout(0, 10));
        isi.setBorder(new EmptyBorder(0, 16, 0, 16));

        JPanel form = new JPanel(new GridBagLayout());                      // Layout 3: GridBagLayout
        form.setBorder(new TitledBorder("Data Mahasiswa"));
        tambahBaris(form, 0, "Nama", tfNama);
        tambahBaris(form, 1, "NIM", tfNim);
        tambahBaris(form, 2, "Program Studi", cbProdi);
        tambahBaris(form, 3, "Semester", spSemester);

        JPanel hobi = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        hobi.add(ckCoding);
        hobi.add(ckGame);
        hobi.add(ckMusik);
        tambahBaris(form, 4, "Hobi", hobi);

        taHasil.setEditable(false);
        taHasil.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(taHasil);
        scroll.setBorder(new TitledBorder("Data Tersimpan"));

        isi.add(form, BorderLayout.NORTH);
        isi.add(scroll, BorderLayout.CENTER);
        return isi;
    }

    // ===== Intermediate Container: footer (tombol + status) =====
    private JPanel buatFooter() {
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnSimpan = buatTombol("Simpan", BIRU, BIRU_HOVER);
        JButton btnReset = buatTombol("Reset", BIRU, BIRU_HOVER);
        JButton btnKeluar = buatTombol("Keluar", MERAH, MERAH_HOVER);
        tombol.add(btnSimpan);
        tombol.add(btnReset);
        tombol.add(btnKeluar);

        // Event 1: ActionListener (interface) -> override actionPerformed()
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpan();
            }
        });
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keluar();
            }
        });

        lblStatus.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(4, 8, 4, 8)));

        JPanel footer = new JPanel(new GridLayout(2, 1));                   // Layout 4: GridLayout
        footer.add(tombol);
        footer.add(lblStatus);
        return footer;
    }

    // ===== Event lain =====
    private void pasangEvent() {
        // Event 2: KeyAdapter -> NIM hanya angka, maksimal 12 digit
        tfNim.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || tfNim.getText().length() >= 12) {
                    e.consume();
                }
            }
        });

        // Event 3: ItemListener (interface) -> override itemStateChanged()
        cbProdi.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    lblStatus.setText(" Prodi dipilih: " + e.getItem());
                }
            }
        });

        // Event 4: WindowAdapter -> konfirmasi saat tombol X ditekan
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keluar();
            }
        });
    }

    // ===== Logika aksi =====
    private void simpan() {
        String nama = tfNama.getText().trim();
        String nim = tfNim.getText().trim();
        if (nama.isEmpty() || nim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan NIM wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StringBuilder hobi = new StringBuilder();
        for (JCheckBox c : new JCheckBox[]{ckCoding, ckGame, ckMusik}) {
            if (c.isSelected()) hobi.append(hobi.length() > 0 ? ", " : "").append(c.getText());
        }
        jumlahData++;
        taHasil.append(jumlahData + ". " + nama + " | " + nim + " | " + cbProdi.getSelectedItem()
                + " | Smt " + spSemester.getValue() + " | " + (hobi.length() > 0 ? hobi : "-") + "\n");
        lblStatus.setText(" Data ke-" + jumlahData + " berhasil disimpan.");
    }

    private void reset() {
        tfNama.setText("");
        tfNim.setText("");
        cbProdi.setSelectedIndex(0);
        spSemester.setValue(1);
        ckCoding.setSelected(false);
        ckGame.setSelected(false);
        ckMusik.setSelected(false);
        lblStatus.setText(" Form direset.");
    }

    private void keluar() {
        int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) System.exit(0);
    }

    // ===== Helper =====
    private void tambahBaris(JPanel p, int baris, String teks, JComponent komponen) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 8, 4, 8);
        g.gridy = baris;
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0;
        p.add(new JLabel(teks), g);
        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(komponen, g);
    }

    private JButton buatTombol(String teks, Color normal, Color hover) {
        JButton b = new JButton(teks);
        b.setBackground(normal);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(95, 32));
        // Event 5: MouseAdapter -> efek hover pada tombol
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(normal);
            }
        });
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LatihanGUI().setVisible(true));
    }
}
