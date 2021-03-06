package admin.gui.glavniProzor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import admin.gui.previewWizard.GenerickiWizard;
import model.Parametar;
import model.Proizvod;
import model.Wizard;
import model.Workspace;

public class PocetniProzor extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -189172659357159009L;
	private static PocetniProzor instance=null;
	private static ResourceBundle resourceBundle;
	private GlavniMeni menu;
		
	public static final String IME_APLIKACIJE = "Universtalator";

	private ToolBar toolbar;
	private ToolBar toolbar1;
	private JScrollPane panStablo;
	private JPanel panStatusLinija;
	private JPanel panEditor;
	private JLabel lblStatusLin;
	private JLabel lbl1;
	private JLabel lbl;
	private JSplitPane sp1;
	private JTree tree;
	private DefaultTreeModel treeModel;
	private Workspace workspace1;

	

	public static PocetniProzor getInstance(){
		if (instance == null){
			instance = new PocetniProzor();
			instance.InitGui();
		}
		
		return instance;
	}
	
	public void changeLanguage() {

		resourceBundle = ResourceBundle.getBundle("gui.MessageResources.MessageResources", Locale.getDefault());
		menu.initComponents();
		
		UIManager.put("OptionPane.yesButtonText", resourceBundle.getObject("yesOption"));
		UIManager.put("OptionPane.noButtonText", resourceBundle.getObject("noOption"));
		UIManager.put("OptionPane.okButtonText", resourceBundle.getObject("okOption"));
		UIManager.put("OptionPane.cancelButtonText", resourceBundle.getObject("cancelOption"));
		
	}
	
		public void InitGui() {
			resourceBundle = ResourceBundle.getBundle("gui.MessageResources.MessageResources", Locale.getDefault());			
		
			 setTitle("Universtalator");
			 setSize(1050,750);
			 setIconImage(new ImageIcon("slike/ikonica.png").getImage());
			 setLocationRelativeTo(null);
	         setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			 setVisible(false);
			 addWindowListener(new WindowListenerPocetniProzor());		
			 
			//KREIRANJE JEDINSTVENOG OBJEKTA WORKSPACE 
			 workspace1=new Workspace();
			 ////////////KREIRANJE MENI STAVKE//////////////////////
			 menu=new GlavniMeni();
		     this.setJMenuBar(menu);
		     
		     ///////////KREIRANJE TOOLBARA//////////////////////
		     toolbar=new ToolBar();
		     toolbar.setBackground(Color.DARK_GRAY);
		     
		     add(toolbar, BorderLayout.NORTH);
		     

		     
		     
		     ///////////KREIRANJE DRVETA/////////////////////
		     DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(PocetniProzor.getInstance().getResourceBundle().getString("dodatno.proizvod"));
			 treeModel = new DefaultTreeModel(rootNode);
			 treeModel.setAsksAllowsChildren(true);
			 tree = new JTree(treeModel);
		     tree.setBackground(new Color(232, 229, 227));
		     tree.addTreeSelectionListener(new SelectionListener());
		     
		     tree.setCellRenderer(new TreeProizvodiRender());
			 panStablo = new JScrollPane(tree);
			 panStablo.setMinimumSize(new Dimension(0,0));
			 
			 ///////////POSTAVLJANJE STATUSNE LINIJE//////////////////////////
			 panStatusLinija = new JPanel();
			 panStatusLinija.setBackground(Color.DARK_GRAY);
			 panStatusLinija.setPreferredSize(new Dimension(10,20));
			 add(panStatusLinija,BorderLayout.SOUTH);
			 
			
			 
			 lblStatusLin = new JLabel(PocetniProzor.getInstance().getResourceBundle().getString("dodatno.opened"));
			 lblStatusLin.setForeground(Color.WHITE);
			 panStatusLinija.add(lblStatusLin);
			 
			 
			 ////////////POSTAVLJANJE PANELA GDE CE STOJATI IZGLED WIZARDA//////////////////////////////////////
			 panEditor=new JPanel();
			 panEditor.setBackground(new Color(82, 82, 82));
			 panEditor.setPreferredSize(new Dimension(300,400));
			 panEditor.setLayout(new BorderLayout());
			 add(panEditor,BorderLayout.WEST);
			
			 
			 
			 ////////////OMOGUCAVANJE SPLIT PANELA IZMEDJU DRVO PANELA I PANEL ZADUZEN ZA PRIKAZ WIZARDA//////////////////////////////////////
			 sp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panStablo,panEditor);
			 sp1.setOneTouchExpandable(true);
			 sp1.setDividerLocation(300);
			 add(sp1);
		}


		private PocetniProzor() { 
			
			 super();
			 
			 
			 
		}
		
		public void popuni_stablo() {
		    Object koren = treeModel.getRoot();
		    DefaultMutableTreeNode root = new DefaultMutableTreeNode(koren);
		    
		    
		    List<Proizvod> list = workspace1.getListaProizvoda();
		    for(int i =0; i<list.size();i++) {
		    	Proizvod proizvod1 = list.get(i);
		    	DefaultMutableTreeNode pnode = new DefaultMutableTreeNode(proizvod1);
		    	
		    	for(int j =0; j<list.get(i).getListaWizarda().size(); j++) {
		    		Wizard wizard1 = list.get(i).getListaWizarda().get(j);
		    		DefaultMutableTreeNode wnode = new DefaultMutableTreeNode(wizard1);
		    		
		    		for(int k = 0; k<list.get(i).getListaWizarda().get(j).getParametri().size(); k++) {
		    			Parametar parametar1 = list.get(i).getListaWizarda().get(j).getParametri().get(k);
		    			DefaultMutableTreeNode p1node = new DefaultMutableTreeNode(parametar1);
		    			wnode.add(p1node);
		    		}
		    		pnode.add(wnode);
		    	}
		    	root.add(pnode);
		    }
		    treeModel.setRoot(root);
		}
			
		// INTERNA KLASA KOJA CE NAM SLUZITI ZA CONFIRM DIJALOG IZLAZA IZ GLAVNOG PROZORA
		
		public class WindowListenerPocetniProzor implements WindowListener{

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				JFrame frame= (JFrame) arg0.getComponent();
				int code=JOptionPane.showConfirmDialog(frame, PocetniProzor.getInstance().getResourceBundle().getString("wizard.zatvaranje"),IME_APLIKACIJE,JOptionPane.YES_NO_OPTION);
				
				if (code!=JOptionPane.YES_OPTION){
				
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
				else{
					frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				}
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

		}
		
		
		
		class SelectionListener implements TreeSelectionListener {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode sel = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
				arg0.getSource();
				
				Object o = null;
				if (sel != null) {
					o = sel.getUserObject();
				}
			
				
				if (sel != null){
					if(o instanceof Wizard ) {
						//SVAKI PUT KADA ON SELEKTUJE WIZARDA
						//TI NAPRAVI NOVI PROZOR WIZARDA I ONDA NA NJEGA DODAJ DUGMAD I ONO IZ PARAMETRA STO IMA
						//DUGMAD DODAJ U ZAVISNOSTI OD BROJA WIZARDA
						DefaultMutableTreeNode proizvodNode = (DefaultMutableTreeNode) sel.getParent();				
						Object proizvod = proizvodNode.getUserObject();
						Proizvod proizvod1 = (Proizvod) proizvod;
						
						
						panEditor.removeAll();
						GenerickiWizard gw=new GenerickiWizard(false,null);
						gw.setVisible(false);
						
						//SADA DEO KODA GDE SE DODAJU DUGMAD
						int duzinaListeWizarda=proizvod1.getListaWizarda().size();
						//AKO JE PRVI WIZARD DODAJ POCETNA DUGMAD,AKO JE KRAJNJI KRAJNJA I AKO JE 
						//SREDISNJI SREDNJA DUGMAD
						if(duzinaListeWizarda==1)
							gw.addDugmadZaJedanJediniWizard(proizvod1);
						else{
							if(proizvod1.getListaWizarda().get(duzinaListeWizarda-1)== (Wizard) o)
								gw.addKrajnjaDugmad(proizvod1,0);
							
							else if(proizvod1.getListaWizarda().get(0)== (Wizard) o)
								gw.addPocetnaDugmad(proizvod1);
							else
								gw.addSredisnjaDugmad(proizvod1,0);
						}
						
						// deo koda koji ubacuje vrednosti iz parametara jednog wizarda u preview tog wizarda
						Wizard cvorWizard = (Wizard) o;
						List<Parametar> parametri =cvorWizard.getParametri();
						for(int i=0; i<parametri.size();i++){
							Parametar parametar1=parametri.get(i);
							if(parametar1.getData_type()=="Image"){
								gw.addImage(parametar1.getValue(),parametar1.getVidljiv(),parametar1.getChangeable(), parametar1.getObavezan());
							}
							else if(parametar1.getData_type()=="One Line Text"){
								gw.addOneLineText(parametar1.getValue(),parametar1.getReadOnly(),parametar1.getVidljiv(), parametar1.getObavezan());
							}
							else if(parametar1.getData_type()=="Multiple Line Text"){
								gw.addMultiLineText(parametar1.getValue(),parametar1.getReadOnly(),parametar1.getVidljiv(), parametar1.getObavezan());
							}
							else if(parametar1.getData_type()=="Boolean"){
								gw.addBoolean(parametar1.getVidljiv(), parametar1.getReadOnly(), parametar1.getObavezan());
							}
							else if(parametar1.getData_type().equals("File Chooser")){
								gw.addFileChooser(parametar1.getVidljiv(), parametar1.getObavezan(),false);
							}else{
								gw.addFileChooser(parametar1.getVidljiv(), parametar1.getObavezan(),true);
							}
						}
						
						panEditor.add(gw.getContentPane(),BorderLayout.CENTER);
						panEditor.revalidate();
						panEditor.repaint();
					}
					else{
						panEditor.removeAll();
						panEditor.revalidate();
						panEditor.repaint();
					}
				}else{
					panEditor.removeAll();
					panEditor.revalidate();
					panEditor.repaint();
				}
				
			}

			
			  }
		
		
		
////////////////////OBICNI GETERI I SETERI////////////////////////////////////////

public ResourceBundle getResourceBundle() {
return resourceBundle;
}


public void setResourceBundle(ResourceBundle resourceBundle) {
this.resourceBundle = resourceBundle;
}


public GlavniMeni getMenu() {
return menu;
}


public void setMenu(GlavniMeni menu) {
this.menu = menu;
}


public ToolBar getToolbar() {
return toolbar;
}


public void setToolbar(ToolBar toolbar) {
this.toolbar = toolbar;
}


public ToolBar getToolbar1() {
return toolbar1;
}


public void setToolbar1(ToolBar toolbar1) {
this.toolbar1 = toolbar1;
}


public JScrollPane getPanStablo() {
return panStablo;
}


public void setPanStablo(JScrollPane panStablo) {
this.panStablo = panStablo;
}


public JPanel getPanStatusLinija() {
repaint();

return panStatusLinija;

}


public void setPanStatusLinija(JPanel panStatusLinija) {
this.panStatusLinija = panStatusLinija;
}


public JPanel getPanEditor() {
return panEditor;
}


public void setPanEditor(JPanel panEditor) {
this.panEditor = panEditor;
}


public JLabel getLblStatusLin() {
return lblStatusLin;
}


public void setLblStatusLin(JLabel lblStatusLin) {
this.lblStatusLin = lblStatusLin;


}


public JLabel getLbl1() {
return lbl1;
}


public void setLbl1(JLabel lbl1) {
this.lbl1 = lbl1;
}


public JLabel getLbl() {
return lbl;
}


public void setLbl(JLabel lbl) {
this.lbl = lbl;
}


public JSplitPane getSp1() {
return sp1;
}


public void setSp1(JSplitPane sp1) {
this.sp1 = sp1;
}


public JTree getTree() {
return tree;
}


public void setTree(JTree tree) {
this.tree = tree;
}


public DefaultTreeModel getTreeModel() {
return treeModel;
}


public void setTreeModel(DefaultTreeModel treeModel) {
this.treeModel = treeModel;
}


public static void setInstance(PocetniProzor instance) {
PocetniProzor.instance = instance;
}


public Workspace getWorkspace1() {
return workspace1;
}


public void setWorkspace1(Workspace workspace1) {
this.workspace1 = workspace1;
}
}
