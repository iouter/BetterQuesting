package betterquesting.commands.admin;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.commands.QuestCommandBase;
import betterquesting.questing.QuestDatabase;
import betterquesting.storage.NameCache;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestCommandCheckCompletion extends QuestCommandBase {
	@Override
	public String getUsageSuffix() {
		return "<username|uuid> <quest_id>";
	}

	@Override
	public boolean validArgs(String[] args) {
		return args.length == 3;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> autoComplete(MinecraftServer server, ICommandSender sender, String[] args) {
		ArrayList<String> list = new ArrayList<>();

		if (args.length == 2) {
			return CommandBase.getListOfStringsMatchingLastWord(args, NameCache.INSTANCE.getAllNames().toArray(new String[0]));
		} else if (args.length == 3) {
			for (DBEntry<IQuest> i : QuestDatabase.INSTANCE.getEntries()) {
				list.add(Integer.toString(i.getID()));
			}
		}

		return list;
	}

	@Override
	public String getCommand() {
		return "check";
	}

	@Override
	public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args) throws CommandException {
		UUID uuid;

		uuid = this.findPlayerID(server, sender, args[1]);

		if (uuid == null) {
			throw this.getException(command);
		}

		String pName = NameCache.INSTANCE.getName(uuid);

		int id = Integer.parseInt(args[2].trim());
		IQuest quest = QuestDatabase.INSTANCE.getValue(id);
		if (quest == null) throw getException(command);
		sender.addChatMessage(new ChatComponentTranslation("betterquesting.cmd.check." + quest.isComplete(uuid), pName, new ChatComponentTranslation(quest.getProperty(NativeProps.NAME))));
	}

	@Override
	public boolean isArgUsername(String[] args, int index) {
		return index == 1;
	}
}
